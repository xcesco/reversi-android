package it.fmt.games.reversi.android.repositories.network;

import androidx.annotation.NonNull;

import it.fmt.games.reversi.android.repositories.model.ErrorEventDispatcher;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;

public interface NetworkClient {
  ConnectedUser connect(final UserRegistration userInfo, final ErrorEventDispatcher errorEventDispatcher);

  void match(ConnectedUser user, @NonNull MatchEventListener listener);

  void disconnect();
}
