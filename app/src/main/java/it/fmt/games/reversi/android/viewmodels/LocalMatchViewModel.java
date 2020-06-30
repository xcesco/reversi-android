package it.fmt.games.reversi.android.viewmodels;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessage;
import it.fmt.games.reversi.android.ui.activities.GameActivity;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.model.Piece;

public class LocalMatchViewModel extends AbstractMatchViewModel {

  private Executor executor = Executors.newSingleThreadExecutor();

  public void match(final GameActivity activity, GameType gameType) {
    Pair<Player1, Player2> players = definePlayers(Piece.PLAYER_1, gameType);
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
