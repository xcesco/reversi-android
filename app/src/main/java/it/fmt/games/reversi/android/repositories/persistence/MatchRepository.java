package it.fmt.games.reversi.android.repositories.persistence;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.NetworkClientImpl;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessage;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;

@Singleton
public class MatchRepository {

  private final NetworkClient networkClient;
  private ConnectedUser connectedUser;

  @Inject
  public MatchRepository(NetworkClient networkClient) {
    this.networkClient = networkClient;
  }

  public void connect(String name) {
    UserRegistration user = new UserRegistration(name);
    connectedUser = networkClient.connect(user);
  }

  public void disconnect() {
    connectedUser = null;
    networkClient.disconnect();
  }

  public LiveData<MatchMessage> match(@NonNull MatchEventListener listener) {
    MutableLiveData<MatchMessage> result = new MutableLiveData<>();

    Executors.newSingleThreadExecutor().execute(() -> {
      networkClient.match(connectedUser, listener);
    });

    return result;
  }
}
