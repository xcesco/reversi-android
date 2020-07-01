package it.fmt.games.reversi.android.viewmodels;

import javax.inject.Inject;

import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.ui.activities.GameActivity;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;
import timber.log.Timber;

public class NetworkMatchViewModel extends AbstractMatchViewModel {

  @Inject
  NetworkClient client;

  public NetworkMatchViewModel() {
    ReversiApplication.getInjector().inject(this);
  }

  @Override
  public void match(final GameActivity activity, GameType gameType) {
    //if (player1 instanceof Network)
    executor.execute(() -> {
      final ConnectedUser user = client.connect(UserRegistration.of("local"));
      final UserInputReader userInputReader = this::readPlayerMove;
      // final GameRenderer gamerRendererWrapper = new AndroidRendererWrapper(this, player1, player2);
      NetworkMatchEventListener listener = new NetworkMatchEventListener(this, userInputReader);
      client.match(user, listener);
    });
  }


  static class NetworkMatchEventListener implements MatchEventListener {
    private final MatchEventDispatcher matchEventDispatcher;
    private final UserInputReader userInputReader;
    private Piece activePiece;

    public NetworkMatchEventListener(MatchEventDispatcher matchEventDispatcher, UserInputReader userInputReader) {
      this.matchEventDispatcher = matchEventDispatcher;
      this.userInputReader = userInputReader;
    }

    @Override
    public void onMatchStart(MatchStartMessage event) {
      activePiece = event.getAssignedPiece();
      MatchStartMessage newMessage = new MatchStartMessage(Piece.PLAYER_1 == activePiece ? PlayerType.HUMAN_PLAYER : PlayerType.NETWORK_PLAYER,
              Piece.PLAYER_2 == activePiece ? PlayerType.HUMAN_PLAYER : PlayerType.NETWORK_PLAYER,
              event.getPlayerId(),
              event.getMatchId(),
              Piece.PLAYER_1);
      matchEventDispatcher.postMatchStart(newMessage);
    }

    @Override
    public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
      Timber.i("Relve che tocca muovere %s %s", event.getGameSnapshot().getActivePiece(), event.getGameSnapshot().getAvailableMoves());
      matchEventDispatcher.postMatchMove(event);

      if (activePiece == event.getGameSnapshot().getActivePiece()) {
        Coordinates move = userInputReader.readInputFor(null,
                event.getGameSnapshot().getAvailableMoves().getMovesActivePlayer());

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
