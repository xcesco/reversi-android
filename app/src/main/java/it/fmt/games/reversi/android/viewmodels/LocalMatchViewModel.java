package it.fmt.games.reversi.android.viewmodels;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.PlayerFactory;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.repositories.model.AndroidDecisionHandler;
import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.ui.activities.GameActivity;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

public class LocalMatchViewModel extends AbstractMatchViewModel {

  private Executor executor = Executors.newSingleThreadExecutor();

  public void match(final GameActivity activity, GameType gameType) {
    Pair<Player1, Player2> players = definePlayers(Piece.PLAYER_1, gameType);
    final Player1 player1 = players.first;
    final Player2 player2 = players.second;
    final MutableLiveData<MatchMessage> result = new MutableLiveData<>();
    final UserInputReader userInputReader = this::readPlayerMove;
    final LocalMatchEventListener listener = new LocalMatchEventListener(result);
    final GameRenderer gamerRendererWrapper = new LocalRendererWrapper(this, player1, player2, listener);

    executor.execute(() -> {
      Reversi reversi = new Reversi(gamerRendererWrapper, userInputReader, player1, player2);
      reversi.play();
    });
  }
}
