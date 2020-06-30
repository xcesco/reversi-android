package it.fmt.games.reversi.android.viewmodels;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;

public class LocalRendererWrapper implements GameRenderer {
  private final MatchEventListener listener;
  private final Player1 player1;
  private final Player2 player2;

  public LocalRendererWrapper(Player1 player1, Player2 player2, MatchEventListener listener) {
    this.player1 = player1;
    this.player2 = player2;
    this.listener = listener;
  }

  boolean started;

  @Override
  public void render(GameSnapshot gameSnapshot) {
    if (gameSnapshot.getStatus() == GameStatus.RUNNING) {
      if (!started) {
        started = true;
        listener.onMatchStart(new MatchStartMessage(null, null, Piece.PLAYER_1));
      }

      listener.onMatchPlayerMove(new MatchStatusMessage(null, null, gameSnapshot));
    } else {
      // render final status
      listener.onMatchPlayerMove(new MatchStatusMessage(null, null, gameSnapshot));
      // propagate result
      listener.onMatchEnd(new MatchEndMessage(null, null, gameSnapshot.getStatus(), gameSnapshot.getScore()));
    }
  }
}