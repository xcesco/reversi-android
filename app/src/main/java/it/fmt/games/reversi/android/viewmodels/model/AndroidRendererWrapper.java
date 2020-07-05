package it.fmt.games.reversi.android.viewmodels.model;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.viewmodels.model.MatchEventDispatcher;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;

public class AndroidRendererWrapper implements GameRenderer {
  private final PlayerType player1Type;
  private final PlayerType player2Type;
  private final MatchEventDispatcher eventDispatcher;

  public AndroidRendererWrapper(MatchEventDispatcher eventDispatcher, Player1 player1, Player2 player2) {
    this.player1Type = player1.isHumanPlayer() ? PlayerType.HUMAN_PLAYER : PlayerType.LOCAL_CPU;
    this.player2Type = player2.isHumanPlayer() ? PlayerType.HUMAN_PLAYER : PlayerType.LOCAL_CPU;
    this.eventDispatcher = eventDispatcher;
  }

  boolean started;

  @Override
  public void render(GameSnapshot gameSnapshot) {
    if (GameStatus.RUNNING == gameSnapshot.getStatus()) {
      if (!started) {
        started = true;
        eventDispatcher.postMatchStart(new MatchStartMessage(player1Type, null, player2Type, null, null, null, Piece.PLAYER_1));
      }
      eventDispatcher.postMatchMove(new MatchStatusMessage(null, null, gameSnapshot));
    } else {
      eventDispatcher.postMatchMove(new MatchStatusMessage(null, null, gameSnapshot));
      // propagate result
      eventDispatcher.postMatchEnd(new MatchEndMessage(null, null, gameSnapshot.getStatus(), gameSnapshot.getScore()));
    }
  }
}