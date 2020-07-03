package it.fmt.games.reversi.android.viewmodels.model;

import org.jetbrains.annotations.NotNull;

import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;

public interface MatchEventDispatcher extends ErrorEventDispatcher {
  void postMatchStart(@NotNull MatchStartMessage matchStartMessage);

  void postMatchMove(@NotNull MatchStatusMessage matchStatusMessage);

  void postMatchEnd(@NotNull MatchEndMessage matchEndMessage);
}
