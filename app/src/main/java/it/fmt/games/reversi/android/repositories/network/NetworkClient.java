package it.fmt.games.reversi.android.repositories.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.fmt.games.reversi.android.BuildConfig;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitor;
import it.fmt.games.reversi.android.repositories.network.model.MatchMove;
import it.fmt.games.reversi.android.repositories.network.model.User;
import it.fmt.games.reversi.android.repositories.network.support.JSONMapperFactory;
import it.fmt.games.reversi.android.repositories.network.support.MatchMessageParser;
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

public class NetworkClient {
  private static final String WS_TOPIC_USER_MATCH_DESTINATION = "/topic/user/{uuid}";
  private static final String WS_APP_USER_DESTINATION = "/app/users/{uuid}/moves";
  public static final String HEADER_TYPE = "type";

  private final ObjectMapper objectMapper;
  private final WebServiceClient webServiceClient;
  private final StompClient stompClient;
  private User connectedUser;
  private Disposable connectionDisposable;
  private Disposable disposable;

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public NetworkClient(String baseUrl, String webSocketUrl) {
    OkHttpClient httpClient = buildHttpClient();

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
    connectionDisposable = stompClient.lifecycle().observeOn(Schedulers.io()).subscribe(lifecycleEvent -> {
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

  @NotNull
  private OkHttpClient buildHttpClient() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

    if (BuildConfig.LOG_ENABLED) {
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
    // rest call
    return new OkHttpClient.Builder()
            .callTimeout(240, TimeUnit.SECONDS)
            .readTimeout(240, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(chain -> {
              Request.Builder ongoing = chain.request().newBuilder();

              ongoing.addHeader("Accept", "application/json");

//              ongoing.addHeader("X-REVERSI-OS", "Android");
//              ongoing.addHeader("X-REVERSI-OSVersion", Build.VERSION.RELEASE + ", SDK " + String.valueOf(Build.VERSION.SDK_INT));
//              ongoing.addHeader("X-REVERSI-App", "FMT Reversi");
//              ongoing.addHeader("X-REVERSI-AppVersion", BuildConfig.VERSION_NAME);

              return chain.proceed(ongoing.build());
            }).addInterceptor(interceptor).build();
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

  public User connect(final ConnectedUser userInfo) throws IOException {
    connectedUser = webServiceClient.connect(userInfo).execute().body();
    stompClient.connect();
    return connectedUser;
  }

  public Completable sendMatchMove(UUID userId, Piece piece, UUID matchId, Coordinates move) {
    try {
      String url = WS_APP_USER_DESTINATION.replace("{uuid}", userId.toString());
      String payload = objectMapper.writeValueAsString(MatchMove.of(matchId, userId, piece, move));
      return stompClient.send(url, payload).observeOn(Schedulers.io());
    } catch (JsonProcessingException e) {
      Timber.e(e);
    }
    return null;
  }

  public User match(final MatchMessageVisitor playerHandler) throws IOException {
    String url = WS_TOPIC_USER_MATCH_DESTINATION.replace("{uuid}", connectedUser.getId().toString());
    Timber.i("Subscribe %s to '%s'", connectedUser.getName(), url);
    disposable = stompClient.topic(url)
            .observeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(stompMessage -> MatchMessageParser.parser(connectedUser, objectMapper, stompMessage, playerHandler));
    return readyToPlay(connectedUser);
  }

  private User readyToPlay(final User user) throws IOException {
    return webServiceClient.executeUserIsReadyToPlay(user.getId().toString()).execute().body();
  }

  public User notReadyToPlay(final User user) throws IOException {
    return webServiceClient.executeUserIsNotReadyToPlay(user.getId().toString()).execute().body();
  }

  public static String getHeaderValue(StompMessage stompMessage, String key) {
    return stompMessage.getStompHeaders()
            .stream().filter(header -> key.equals(header.getKey()))
            .findFirst().map(header -> header.getValue()).orElse(null);
  }

}
