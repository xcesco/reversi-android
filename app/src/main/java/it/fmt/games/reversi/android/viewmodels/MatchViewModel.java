package it.fmt.games.reversi.android.viewmodels;

import androidx.lifecycle.LiveData;

import it.fmt.games.reversi.android.repositories.network.model.ErrorStatus;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.viewmodels.model.CpuType;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.model.Coordinates;

public interface MatchViewModel {
  LiveData<MatchStartMessage> onStartMessage();

  LiveData<MatchStatusMessage> onStatusMessage();

  LiveData<MatchEndMessage> onEndMessage();

  LiveData<ErrorStatus> onErrorStatus();

  void match(String playerName, GameType gameType, CpuType cpuType);

  boolean readUserMove(Coordinates coordinate);
}
