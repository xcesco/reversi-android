package it.fmt.games.reversi.android.viewmodels;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.PlayerFactory;
import it.fmt.games.reversi.android.repositories.network.model.ErrorStatus;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.android.viewmodels.model.AndroidDecisionHandler;
import it.fmt.games.reversi.android.viewmodels.model.CpuType;
import it.fmt.games.reversi.android.viewmodels.model.MatchEventDispatcher;
import it.fmt.games.reversi.android.viewmodels.model.Move;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;
import it.fmt.games.reversi.model.Player;
import timber.log.Timber;

public abstract class AbstractMatchViewModel extends ViewModel implements MatchEventDispatcher, MatchViewModel {
  private final Move userMove = new Move();
  private GameSnapshot latestGameshot;
  private MutableLiveData<MatchStartMessage> startMessageLiveData = new MutableLiveData<>();
  private MutableLiveData<MatchEndMessage> endMessageLiveData = new MutableLiveData<>();
  private MutableLiveData<MatchStatusMessage> statusMessageLiveData = new MutableLiveData<>();
  private MutableLiveData<ErrorStatus> errorStatusLiveData = new MutableLiveData<>();

  protected static Pair<Player1, Player2> definePlayers(Piece assignedPiece, GameType gameType, CpuType cpuType) {
    Player1 player1 = null;
    Player2 player2 = null;

    switch (gameType) {
      case PLAYER_VS_PLAYER:
        player1 = PlayerFactory.createHumanPlayer1();
        player2 = PlayerFactory.createHumanPlayer2();
        break;
      case PLAYER_VS_CPU:
        player1 = PlayerFactory.createHumanPlayer1();
        player2 = PlayerFactory.createCpuPlayer2(new AndroidDecisionHandler(cpuType));
        break;
      case CPU_VS_PLAYER:
        player1 = PlayerFactory.createCpuPlayer1(new AndroidDecisionHandler(cpuType));
        player2 = PlayerFactory.createHumanPlayer2();
        break;
      case CPU_VS_CPU:
        player1 = PlayerFactory.createCpuPlayer1(new AndroidDecisionHandler(cpuType));
        player2 = PlayerFactory.createCpuPlayer2(new AndroidDecisionHandler(cpuType));
        break;
      case PLAYER_VS_NETWORK:
      default:
        break;
    }

    return Pair.create(player1, player2);
  }

  public LiveData<MatchStartMessage> onStartMessage() {
    return startMessageLiveData;
  }

  public LiveData<MatchEndMessage> onEndMessage() {
    return endMessageLiveData;
  }

  public LiveData<MatchStatusMessage> onStatusMessage() {
    return statusMessageLiveData;
  }

  public LiveData<ErrorStatus> onErrorStatus() {
    return errorStatusLiveData;
  }

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

  public abstract void match(String playerName, GameType gameType, CpuType cpuType);

  public boolean readUserMove(Coordinates coordinate) {
    boolean validMove = latestGameshot != null && latestGameshot.getAvailableMoves().getMovesActivePlayer().contains(coordinate);
    Timber.d("try to set move in %s = %s", coordinate, validMove);
    if (!validMove) return false;
    synchronized (userMove) {
      userMove.setCoordinates(coordinate);
      userMove.notifyAll();
    }
    return true;
  }

  @Override
  public void postMatchStart(@NotNull MatchStartMessage matchStartMessage) {
    this.startMessageLiveData.postValue(matchStartMessage);
  }

  @Override
  public void postMatchMove(@NotNull MatchStatusMessage matchStatusMessage) {
    this.latestGameshot = matchStatusMessage.getGameSnapshot();
    this.statusMessageLiveData.postValue(matchStatusMessage);
  }

  @Override
  public void postMatchEnd(@NotNull MatchEndMessage matchEndMessage) {
    this.endMessageLiveData.postValue(matchEndMessage);
  }

  @Override
  public void postErrorStatus(@NotNull ErrorStatus errorStatus) {
    this.errorStatusLiveData.postValue(errorStatus);
  }

}
