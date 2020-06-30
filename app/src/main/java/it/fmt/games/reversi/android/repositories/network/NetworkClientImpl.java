package it.fmt.games.reversi.android.repositories.network;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.fmt.games.reversi.android.exceptions.ReversiRuntimeException;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitor;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitorImpl;
import it.fmt.games.reversi.android.repositories.network.model.MatchMove;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.repositories.network.model.UserStatus;
import it.fmt.games.reversi.android.repositories.network.support.HttpClientBuilder;
import it.fmt.games.reversi.android.repositories.network.support.JSONMapperFactory;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

import static it.fmt.games.reversi.android.repositories.network.support.MatchMessageParser.parser;

public class NetworkClientImpl implements NetworkClient {
  public static final String HEADER_TYPE = "type";
  private static final String WS_TOPIC_USER_MATCH_DESTINATION = "/topic/user/{uuid}";
  private static final String WS_USER_STATUS_REPLY = "/user/status";
  private static final String WS_APP_USER_DESTINATION = "/app/users/{uuid}/moves";
  private static final String WS_APP_USER_READY_DESTINATION = "/app/users/{uuid}/ready";
  private static final String WS_APP_USER_NOT_READY_DESTINATION = "/app/users/{uuid}/not-ready";
  private final ObjectMapper objectMapper;
  private final WebServiceClient webServiceClient;
  private final StompClient webSocketClient;
  private Disposable connectionDisposable;

  public NetworkClientImpl(final String serverUrl) {
    final String webSocketBaseUrl = serverUrl.replace("https", "wss");
    OkHttpClient httpClient = HttpClientBuilder.buildHttpClient();

    objectMapper = buildObjectMapper();

    Retrofit retrofitJackson = new Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(httpClient).build();

    webServiceClient = retrofitJackson.create(WebServiceClient.class);
    webSocketClient = buildStompOverWebSocketClient(webSocketBaseUrl, httpClient);
  }

  public static String getHeaderValue(StompMessage stompMessage, String key) {
    return stompMessage.getStompHeaders()
            .stream().filter(header -> key.equals(header.getKey()))
            .findFirst().map(header -> header.getValue()).orElse(null);
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
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

  public void disconnect() {
    webSocketClient.disconnect();

    if (connectionDisposable != null) {
      connectionDisposable.dispose();
    }
  }


  public ConnectedUser connect(final UserRegistration userInfo) {
    try {
      connectWebSocket();
      return webServiceClient.connect(userInfo).execute().body();
    } catch (IOException e) {
      Timber.e(e);
      throw new ReversiRuntimeException(e);
    }
  }

  @Override
  public void match(ConnectedUser user, @NonNull MatchEventListener listener) {
    this.matchInternal(user, listener);
  }

  void connectWebSocket() {
    webSocketClient.connect();
  }

  public Completable sendMatchMove(UUID userId, Piece piece, UUID matchId, Coordinates move) {
    try {
      String url = WS_APP_USER_DESTINATION.replace("{uuid}", userId.toString());
      String payload = objectMapper.writeValueAsString(MatchMove.of(matchId, userId, piece, move));
      Timber.i("%s send message %s", piece, payload);
      return webSocketClient.send(url, payload).observeOn(Schedulers.io()).subscribeOn(Schedulers.computation());
    } catch (JsonProcessingException e) {
      Timber.e(e);
    }
    return null;
  }

  private Disposable sendUserStatus(final ConnectedUser user, boolean ready) {
    String url;
    UserStatus status;
    if (ready) {
      url = WS_APP_USER_READY_DESTINATION.replace("{uuid}", user.getId().toString());
      status = UserStatus.READY_TO_PLAY;
    } else {
      url = WS_APP_USER_NOT_READY_DESTINATION.replace("{uuid}", user.getId().toString());
      status = UserStatus.NOT_READY_TO_PLAY;
    }

    Timber.i("%s send %s to %s", user.getName(), status, url);
    return webSocketClient.send(url, "")
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.computation())
            .subscribe(() -> {
              Timber.i("%s set status %s", user.getName(), status);
            });
  }

  private Disposable sendUserReady(ConnectedUser user) {
    return sendUserStatus(user, true);
  }

  private Disposable sendUserNotReady(ConnectedUser user) {
    return sendUserStatus(user, false);
  }

  private Flowable<StompMessage> observeUserStatus(ConnectedUser user) {
    Timber.d("%s observer %s", user.getName(), WS_USER_STATUS_REPLY);
    return webSocketClient.topic(WS_USER_STATUS_REPLY)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.computation());
  }

  public void matchInternal(@NonNull ConnectedUser user, @NonNull MatchEventListener listener) {
    final MatchMessageVisitorImpl playerHandler = new NetworkPlayerHandler(this, user, listener);
    CompletableFuture<ConnectedUser> userReadyCompletable = new CompletableFuture<>();
    CompletableFuture<ConnectedUser> userAwaitingToStartCompletable = new CompletableFuture<>();
    CompletableFuture<ConnectedUser> userInGameCompletable = new CompletableFuture<>();
    CompletableFuture<ConnectedUser> userNotReadyCompletable = new CompletableFuture<>();

    // prepare user to match
    Disposable watchUserStatusDisposable = watchUserStatus(user,
            userReadyCompletable,
            userAwaitingToStartCompletable,
            userInGameCompletable,
            userNotReadyCompletable);

    final ConnectedUser player = user;
    String url = WS_TOPIC_USER_MATCH_DESTINATION.replace("{uuid}", user.getId().toString());
    Disposable watchMatchDisposable = webSocketClient
            .topic(url)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.computation())
            .subscribe(stompMessage -> parser(player, objectMapper, stompMessage, playerHandler));

    Disposable sendUserReadDisposable = sendUserReady(user);

    try {
      // user AWAITING TO START
      user = userAwaitingToStartCompletable.get();
    } catch (ExecutionException | InterruptedException e) {
      Timber.e(e);
      throw new ReversiRuntimeException(e);
    }

    MatchEndMessage finalMessage;
    try {
      // match end event
      finalMessage = playerHandler.getMatchEndMessage();
    } catch (ExecutionException | InterruptedException e) {
      Timber.e(e);
      throw new ReversiRuntimeException(e);
    }
    Timber.i("Match finished");
    Timber.i("%s receives result %s: %s - %s ", user.getName(),
            finalMessage.getStatus(),
            finalMessage.getScore().getPlayer1Score(),
            finalMessage.getScore().getPlayer2Score());

    Disposable sendUserNotReadyDisposable = sendUserNotReady(user);

    try {
      // user AWAITING TO START
      user = userNotReadyCompletable.get();
      Timber.i("%s has final status %s ", user.getName(), user.getStatus());
    } catch (ExecutionException | InterruptedException e) {
      Timber.e(e);
      throw new ReversiRuntimeException(e);
    }

    sendUserReadDisposable.dispose();
    watchUserStatusDisposable.dispose();
    watchMatchDisposable.dispose();
    sendUserNotReadyDisposable.dispose();
  }

  private Disposable watchUserStatus(ConnectedUser user,
                                     final CompletableFuture<ConnectedUser> userReadyCompletable,
                                     final CompletableFuture<ConnectedUser> userAwaitingToStartCompletable,
                                     final CompletableFuture<ConnectedUser> userInGameFuture,
                                     final CompletableFuture<ConnectedUser> userNotReadyCompletable) {
    return observeUserStatus(user).subscribe(stompMessage -> {
      ConnectedUser userChanged = getObjectMapper().readValue(stompMessage.getPayload(), ConnectedUser.class);
      Timber.i("%s receives user status is %s", userChanged.getName(), userChanged.getStatus());

      switch (userChanged.getStatus()) {
        case READY_TO_PLAY:
          userReadyCompletable.complete(userChanged);
          break;
        case AWAITNG_TO_START:
          userAwaitingToStartCompletable.complete(userChanged);
          break;
        case IN_GAME:
          userInGameFuture.complete(userChanged);
          break;
        case NOT_READY_TO_PLAY:
          userNotReadyCompletable.complete(userChanged);
          break;
        default:
          break;
      }
    });
  }

}
