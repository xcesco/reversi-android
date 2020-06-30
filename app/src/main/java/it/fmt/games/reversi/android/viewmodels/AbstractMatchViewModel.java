package it.fmt.games.reversi.android.viewmodels;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.PlayerFactory;
import it.fmt.games.reversi.android.repositories.model.AndroidDecisionHandler;
import it.fmt.games.reversi.android.repositories.model.Move;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessage;
import it.fmt.games.reversi.android.ui.activities.GameActivity;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;
import it.fmt.games.reversi.model.Player;

public abstract class AbstractMatchViewModel extends ViewModel {
  private final Move userMove = new Move();

  protected Coordinates readPlayerMove(Player player, List<Coordinates> list) {
    synchronized (userMove) {
      try {
        while (isInvalidMove(list)) {
          userMove.wait();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      return userMove.getCoordinates();
    }
  }

  private boolean isInvalidMove(List<Coordinates> list) {
    return userMove.getCoordinates() == null || list.indexOf(userMove.getCoordinates()) == -1;
  }

  public abstract LiveData<MatchMessage> match(final GameActivity activity, GameType gameType);

  public void readUserMove(Coordinates coordinate) {
    synchronized (userMove) {
      userMove.setCoordinates(coordinate);
      userMove.notifyAll();
    }
  }

  protected static Pair<Player1, Player2> definePlayers(Piece assignedPiece, GameType gameType) {
    Player1 player1 = null;
    Player2 player2 = null;

    switch (gameType) {
      case PLAYER_VS_PLAYER:
        player1 = PlayerFactory.createHumanPlayer1();
        player2 = PlayerFactory.createHumanPlayer2();
        break;
      case PLAYER_VS_CPU:
        player1 = PlayerFactory.createHumanPlayer1();
        player2 = PlayerFactory.createCpuPlayer2(new AndroidDecisionHandler());
        break;
      case CPU_VS_PLAYER:
        player1 = PlayerFactory.createCpuPlayer1(new AndroidDecisionHandler());
        player2 = PlayerFactory.createHumanPlayer2();
        break;
      case CPU_VS_CPU:
        player1 = PlayerFactory.createCpuPlayer1(new AndroidDecisionHandler());
        player2 = PlayerFactory.createCpuPlayer2(new AndroidDecisionHandler());
        break;
      case PLAYER_VS_NETWORK:
      default:
        break;
    }

    return Pair.create(player1, player2);
  }
}
