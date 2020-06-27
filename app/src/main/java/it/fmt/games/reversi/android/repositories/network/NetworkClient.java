package it.fmt.games.reversi.android.repositories.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.fmt.games.reversi.android.BuildConfig;
import it.fmt.games.reversi.android.exceptions.ReversiRuntimeException;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitor;
import it.fmt.games.reversi.android.repositories.network.model.MatchMove;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.UserStatus;
import it.fmt.games.reversi.android.repositories.network.support.HttpClientBuilder;
import it.fmt.games.reversi.android.repositories.network.support.JSONMapperFactory;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

import static it.fmt.games.reversi.android.repositories.network.support.MatchMessageParser.*;

public class NetworkClient {
  private static final String WS_TOPIC_USER_MATCH_DESTINATION = "/topic/user/{uuid}";
  private static final String WS_USER_STATUS_REPLY = "/user/status";
  private static final String WS_APP_USER_DESTINATION = "/app/users/{uuid}/moves";
  private static final String WS_APP_USER_READY_DESTINATION = "/app/users/{uuid}/ready";

  public static final String HEADER_TYPE = "type";

  private final ObjectMapper objectMapper;
  private final WebServiceClient webServiceClient;
  private final StompClient stompClient;
  private ConnectedUser connectedUser;
  private Disposable connectionDisposable;
  private Disposable disposable;

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public NetworkClient(String baseUrl, String webSocketUrl) {
    OkHttpClient httpClient = HttpClientBuilder.buildHttpClient();

    objectMapper = buildObjectMapper();

    Retrofit retrofitJackson = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(httpClient).build();

    webServiceClient = retrofitJackson.create(WebServiceClient.class);
    stompClient = buildStompOverWebSocketClient(webSocketUrl, httpClient);
  }

  private StompClient buildStompOverWebSocketClient(String webSocketUrl, OkHttpClient httpClient) {
    webSocketUrl += "api/messages/websocket/";
    StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, webSocketUrl, null, httpClient);
    connectionDisposable = stompClient.lifecycle().observeOn(Schedulers.io()).subscribeOn(Schedulers.computation()).subscribe(lifecycleEvent -> {
      switch (lifecycleEvent.getType()) {
        case OPENED:
          Timber.d("Stomp connection OPENED");
          break;
        case ERROR:
          Timber.d(lifecycleEvent.getException(), "Error");
          break;
        case CLOSED:
          Timber.d("Stomp connection CLOSED");
          break;
        case FAILED_SERVER_HEARTBEAT:
          Timber.d("Stomp connection FAILED_SERVER_HEARTBEAT");
          break;
      }
    });

    return stompClient;
  }

  private ObjectMapper buildObjectMapper() {
    ObjectMapper objectMapper = JSONMapperFactory.createMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return objectMapper;
  }

  public void diconnect() {
    stompClient.disconnect();

    if (connectionDisposable != null) {
      connectionDisposable.dispose();
    }
    if (disposable != null) {
      disposable.dispose();
    }
  }

  public ConnectedUser connect(final UserRegistration userInfo) {
    try {
      connectedUser = webServiceClient.connect(userInfo).execute().body();
    } catch (IOException e) {
      Timber.e(e);
      throw new ReversiRuntimeException(e);
    }
    connectWebSocket();
    return connectedUser;
  }

  void connectWebSocket() {
    stompClient.connect();
  }

  public Completable sendMatchMove(UUID userId, Piece piece, UUID matchId, Coordinates move) {
    try {
      String url = WS_APP_USER_DESTINATION.replace("{uuid}", userId.toString());
      String payload = objectMapper.writeValueAsString(MatchMove.of(matchId, userId, piece, move));
      Timber.i("%s send message %s", connectedUser.getName(), payload);
      return stompClient.send(url, payload).observeOn(Schedulers.io()).subscribeOn(Schedulers.computation());
    } catch (JsonProcessingException e) {
      Timber.e(e);
    }
    return null;
  }


  private Completable sendUserReady(UUID userId) {
    String url = WS_APP_USER_READY_DESTINATION.replace("{uuid}", userId.toString());
    Timber.i("%s send %s to %s", connectedUser.getName(), UserStatus.READY_TO_PLAY, url);
    return stompClient.send(url, "")
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.computation());
  }

  private Flowable<StompMessage> observeUserStatus() {
    return stompClient.topic(WS_USER_STATUS_REPLY)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.computation());
  }

  public void match(final MatchMessageVisitor playerHandler) {
    String url = WS_TOPIC_USER_MATCH_DESTINATION.replace("{uuid}", connectedUser.getId().toString());

    // prepare user to match
    final ConnectedUser user;
    try {
      user = prepareUserToMatch();
    } catch (ExecutionException | InterruptedException | IOException e) {
      Timber.e(e);
      throw new ReversiRuntimeException(e);
    }
    disposable = stompClient
            .topic(url)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.computation())
            .subscribe(stompMessage -> parser(connectedUser, objectMapper, stompMessage, playerHandler));

    MatchEndMessage finalMessage;
    try {
      finalMessage = playerHandler.getMatchEndMessage();
    } catch (ExecutionException | InterruptedException e) {
      Timber.e(e);
      throw new ReversiRuntimeException(e);
    }
    Timber.i("%s receives result %s: %s - %s ", user.getName(),
            finalMessage.getStatus(),
            finalMessage.getScore().getPlayer1Score(),
            finalMessage.getScore().getPlayer2Score());
    try {
      notReadyToPlay(user);
    } catch (IOException e) {
      Timber.e(e);
      throw new ReversiRuntimeException(e);
    }
    disposable.dispose();
  }

  ConnectedUser prepareUserToMatch() throws ExecutionException, InterruptedException, IOException {
    final CompletableFuture<ConnectedUser> userInGameFuture = new CompletableFuture<>();
    // observe to user status
    Disposable disposableStatus = observeUserStatus().subscribe(stompMessage -> {
      ConnectedUser userChanged = getObjectMapper().readValue(stompMessage.getPayload(), ConnectedUser.class);
      Timber.i("player %s receives user status is %s", userChanged.getName(), userChanged.getStatus());

      switch (userChanged.getStatus()) {
        case AWAITNG_TO_START:
          userInGameFuture.complete(userChanged);
          break;
        default:
          break;
      }
    });

    // tell to server user is ready to play
    Disposable disposableUserReady = sendUserReady(connectedUser.getId())
            .subscribe(() -> Timber.i("%s set status %s", connectedUser.getName(), UserStatus.READY_TO_PLAY));

    // wait user ready to play
    final ConnectedUser userInGame = userInGameFuture.get();
    disposableStatus.dispose();
    disposableUserReady.dispose();

    return userInGame;
  }

  private ConnectedUser readyToPlay(final ConnectedUser user) throws IOException {
    return webServiceClient.executeUserIsReadyToPlay(user.getId().toString()).execute().body();
  }

  public ConnectedUser notReadyToPlay(final ConnectedUser user) throws IOException {
    return webServiceClient.executeUserIsNotReadyToPlay(user.getId().toString()).execute().body();
  }

  public static String getHeaderValue(StompMessage stompMessage, String key) {
    return stompMessage.getStompHeaders()
            .stream().filter(header -> key.equals(header.getKey()))
            .findFirst().map(header -> header.getValue()).orElse(null);
  }

}
