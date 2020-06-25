package it.fmt.reversi.android.repositories.network;

import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitorImpl;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.User;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;
import timber.log.Timber;

public class TestNetworkPlayerHandler extends MatchMessageVisitorImpl {
  private final NetworkClient client;
  private final User user;

  public TestNetworkPlayerHandler(NetworkClient client, User user) {
    this.client = client;
    this.user=user;
  }

  @Override
  public void visit(MatchStartMessage message) {
    Timber.i("player %s receives match start", user.getName());
  }

  @Override
  public void visit(MatchStatusMessage message) {
    Timber.i("player %s receives match status", user.getName());
    GameSnapshot gameSnapshot = message.getGameSnapshot();
    Piece playerPiece = gameSnapshot.getActivePiece();
    Coordinates move = gameSnapshot.getAvailableMoves().getMovesActivePlayer().get(0);

    if (gameSnapshot.getStatus() == GameStatus.RUNNING) {
      Timber.i("user %s decides to move on %s", playerPiece, move);
      client.sendMatchMove(message.getPlayerId(), gameSnapshot.getActivePiece(), message.getMatchId(), move);
    } else {
      Timber.i("player %s detects match has status %s %s - %s",
              playerPiece, gameSnapshot.getStatus(),
              gameSnapshot.getScore().getPlayer1Score(),
              gameSnapshot.getScore().getPlayer2Score()
      );
    }
  }

  @Override
  public void visit(MatchEndMessage message) {
    Timber.i("player %s receives match end", user.getName());
  }
}
