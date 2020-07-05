package it.fmt.games.reversi.android.repositories.network;

import android.util.Pair;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.fmt.games.reversi.android.viewmodels.model.ErrorEventDispatcher;
import it.fmt.games.reversi.android.repositories.network.model.ErrorStatus;
import okhttp3.OkHttpClient;
import timber.log.Timber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public abstract class StompClientBuilder {
  private StompClientBuilder() {

  }

  public static Pair<StompClient, Disposable> build(String webSocketUrl, OkHttpClient httpClient, ErrorEventDispatcher errorEventDispatcher) {
    webSocketUrl += "api/messages/websocket/";
    StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, webSocketUrl, null, httpClient);
    Disposable connectionDisposable = stompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(lifecycleEvent -> {
              switch (lifecycleEvent.getType()) {
                case OPENED:
                  Timber.d("Stomp connection OPENED");
                  break;
                case ERROR:
                  Timber.d(lifecycleEvent.getException(), "Error");
                  if (errorEventDispatcher != null) {
                    errorEventDispatcher.postErrorStatus(new ErrorStatus());
                  }
                  break;
                case CLOSED:
                  Timber.d("Stomp connection CLOSED");
                  break;
                case FAILED_SERVER_HEARTBEAT:
                  Timber.d("Stomp connection FAILED_SERVER_HEARTBEAT");
                  break;
              }
            });

    return Pair.create(stompClient, connectionDisposable);
  }
}
