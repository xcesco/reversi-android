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

public class NetworkMatchViewModel extends AbstractMatchViewModel {

  @Inject
  NetworkClient client;

  public NetworkMatchViewModel() {
    ReversiApplication.getInjector().inject(this);
  }

  @Override
  public void match(final GameActivity activity, GameType gameType) {
    //if (player1 instanceof Network)
    final ConnectedUser user = client.connect(UserRegistration.of("player2"));
    final UserInputReader userInputReader = this::readPlayerMove;
    //final GameRenderer gamerRendererWrapper = new AndroidRendererWrapper(this, player1, player2);
    NetworkMatchEventListener listener = new NetworkMatchEventListener(this, userInputReader);
    client.match(user, listener);
  }

  static class NetworkMatchEventListener implements MatchEventListener {
    private final MatchEventDispatcher matchEventDispatcher;
    private final UserInputReader userInputReader;

    public NetworkMatchEventListener(MatchEventDispatcher matchEventDispatcher, UserInputReader userInputReader) {
      this.matchEventDispatcher = matchEventDispatcher;
      this.userInputReader = userInputReader;
    }

    @Override
    public void onMatchStart(MatchStartMessage event) {
      MatchStartMessage newMessage = new MatchStartMessage(Piece.PLAYER_1 == event.getAssignedPiece() ? PlayerType.HUMAN_PLAYER : PlayerType.NETWORK_PLAYER,
              Piece.PLAYER_2 == event.getAssignedPiece() ? PlayerType.HUMAN_PLAYER : PlayerType.NETWORK_PLAYER,
              event.getPlayerId(),
              event.getMatchId(),
              Piece.PLAYER_1);

      matchEventDispatcher.postMatchStart(newMessage);
    }

    @Override
    public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
      Coordinates move = userInputReader.readInputFor(null,
              event.getGameSnapshot().getAvailableMoves().getMovesActivePlayer());
      matchEventDispatcher.postMatchMove(event);
      return move;
    }

    @Override
    public void onMatchEnd(MatchEndMessage event) {
      matchEventDispatcher.postMatchEnd(event);
    }
  }
}
