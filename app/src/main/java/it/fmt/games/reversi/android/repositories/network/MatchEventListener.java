package it.fmt.games.reversi.android.repositories.network;

import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.model.Coordinates;

public interface MatchEventListener {
  void onMatchStart(MatchStartMessage event);

  Coordinates onMatchPlayerMove(MatchStatusMessage event);

  void onMatchEnd(MatchEndMessage event);
}
