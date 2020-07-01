package it.fmt.games.reversi.android.repositories.network;

import androidx.annotation.NonNull;

import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitorImpl;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;
import timber.log.Timber;

public class NetworkPlayerHandler extends MatchMessageVisitorImpl {
  private final NetworkClientImpl client;
  private final ConnectedUser user;
  private final MatchEventListener listener;
  private Piece assignedPiece;

  public NetworkPlayerHandler(@NonNull NetworkClientImpl client, @NonNull ConnectedUser user, @NonNull MatchEventListener listener) {
    this.client = client;
    this.user = user;
    this.listener = listener;
  }

  @Override
  public void visit(MatchStartMessage message) {
    Timber.i("player %s receives match start", user.getName());
    assignedPiece = message.getAssignedPiece();
    listener.onMatchStart(message);
  }

  @Override
  public void visit(MatchStatusMessage message) {
    Timber.i("%s receives match status", user.getName());
    GameSnapshot gameSnapshot = message.getGameSnapshot();
    Piece playerPiece = gameSnapshot.getActivePiece();

    Coordinates move = listener.onMatchPlayerMove(message);
    if (gameSnapshot.getStatus() == GameStatus.RUNNING) {
      if (gameSnapshot.getActivePiece() == assignedPiece && gameSnapshot.getAvailableMoves().getMovesActivePlayer().size() > 0) {
        Timber.i("user %s decides to move on %s", playerPiece, move);
        client.sendMatchMove(message.getPlayerId(), gameSnapshot.getActivePiece(), message.getMatchId(), move).subscribe();
      } else {
        Timber.i("user %s do nothing", user.getName());
      }
    } else {
      Timber.i("player %s detects match has status %s",
              playerPiece, gameSnapshot.getStatus()
      );
    }
  }

  @Override
  public void visit(MatchEndMessage message) {
    Timber.i("player %s receives match end: %s (%s - %s)", user.getName(), message.getStatus(),
            message.getScore().getPlayer1Score(), message.getScore().getPlayer2Score());
    super.visit(message);
    listener.onMatchEnd(message);

  }
}
