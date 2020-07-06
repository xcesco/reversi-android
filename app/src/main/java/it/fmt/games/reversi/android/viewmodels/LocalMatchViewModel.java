package it.fmt.games.reversi.android.viewmodels;

import androidx.core.util.Pair;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.viewmodels.model.CpuType;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.android.viewmodels.model.AndroidRendererWrapper;
import it.fmt.games.reversi.model.Piece;

public class LocalMatchViewModel extends AbstractMatchViewModel {

  public LocalMatchViewModel() {
    ReversiApplication.getInjector().inject(this);
  }

  public void match(String playerName, GameType gameType, CpuType cpuType) {
    Pair<Player1, Player2> players = definePlayers(Piece.PLAYER_1, gameType, cpuType);
    final Player1 player1 = players.first;
    final Player2 player2 = players.second;
    final UserInputReader userInputReader = this::readPlayerMove;
    final GameRenderer gamerRendererWrapper = new AndroidRendererWrapper(this, player1, player2);

    executor.execute(() -> {
      Reversi reversi = new Reversi(gamerRendererWrapper, userInputReader, player1, player2);
      reversi.play();
    });
  }
}
