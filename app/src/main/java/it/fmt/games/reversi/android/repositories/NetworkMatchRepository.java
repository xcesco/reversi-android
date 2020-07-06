package it.fmt.games.reversi.android.repositories;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.BuildConfig;
import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.exceptions.ReversiRuntimeException;
import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.ErrorStatus;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.viewmodels.model.MatchEventDispatcher;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;
import it.fmt.games.reversi.model.cpu.RandomDecisionHandler;
import timber.log.Timber;

@Singleton
public class NetworkMatchRepository {
  protected ExecutorService executor = Executors.newCachedThreadPool();
  @Inject
  NetworkClient client;

  @Inject
  public NetworkMatchRepository() {
    ReversiApplication.getInjector().inject(this);

  }

  public void match(final MatchEventDispatcher matchEventDispatcher, String playerName, final UserInputReader userInputReader) {
    executor.execute(() -> {
      try {
        final ConnectedUser user = client.connect(UserRegistration.of(playerName), matchEventDispatcher);
        if (user == null) throw new ReversiRuntimeException();
        NetworkMatchEventListener listener = new NetworkMatchEventListener(matchEventDispatcher, userInputReader);
        client.match(user, listener);
      } catch (Exception e) {
        matchEventDispatcher.postErrorStatus(new ErrorStatus());
      }
    });
  }

  public void onCleared() {
    executor.shutdown();
    executor = Executors.newCachedThreadPool();
  }

  static class NetworkMatchEventListener implements MatchEventListener {
    private final MatchEventDispatcher matchEventDispatcher;
    private final UserInputReader userInputReader;
    private Piece assignedPiece;

    public NetworkMatchEventListener(MatchEventDispatcher matchEventDispatcher, UserInputReader userInputReader) {
      this.matchEventDispatcher = matchEventDispatcher;
      this.userInputReader = userInputReader;
    }

    @Override
    public void onMatchStart(MatchStartMessage event) {
      assignedPiece = event.getAssignedPiece();
      MatchStartMessage newMessage = new MatchStartMessage(Piece.PLAYER_1 == assignedPiece ? PlayerType.HUMAN_PLAYER : PlayerType.NETWORK_PLAYER,
              event.getPlayer1Name(),
              Piece.PLAYER_2 == assignedPiece ? PlayerType.HUMAN_PLAYER : PlayerType.NETWORK_PLAYER,
              event.getPlayer2Name(),
              event.getPlayerId(),
              event.getMatchId(),
              assignedPiece);
      matchEventDispatcher.postMatchStart(newMessage);
    }

    @Override
    public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
      GameSnapshot gameSnapshot = event.getGameSnapshot();
      Timber.i("Tocca muovere a %s %s", gameSnapshot.getActivePiece(), gameSnapshot.getAvailableMoves().getMovesActivePlayer());
      matchEventDispatcher.postMatchMove(event);

      if (assignedPiece == gameSnapshot.getActivePiece() && gameSnapshot.getAvailableMoves().isAvailableMovesForActivePlayer()) {
        Coordinates move;
        if (BuildConfig.AUTOMATIC_NETWORK_PLAYER) {
          RandomDecisionHandler handler = new RandomDecisionHandler();
          move = handler.compute(gameSnapshot.getAvailableMoves().getMovesActivePlayer());
        } else {
          move = userInputReader.readInputFor(null,
                  event.getGameSnapshot().getAvailableMoves().getMovesActivePlayer());
        }

        return move;
      } else {
        // it turn of other player. Do nothing
        return null;
      }
    }

    @Override
    public void onMatchEnd(MatchEndMessage event) {
      matchEventDispatcher.postMatchEnd(event);
    }
  }
}
