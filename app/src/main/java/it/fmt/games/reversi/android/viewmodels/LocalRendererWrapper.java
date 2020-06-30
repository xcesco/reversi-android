package it.fmt.games.reversi.android.viewmodels;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;

public class LocalRendererWrapper implements GameRenderer {
  private final MatchEventListener listener;
  private final PlayerType player1Type;
  private final PlayerType player2Type;
  private final AbstractMatchViewModel viewModel;

  public LocalRendererWrapper(AbstractMatchViewModel viewModel, Player1 player1, Player2 player2, MatchEventListener listener) {
    this.player1Type = player1.isHumanPlayer() ? PlayerType.LOCAL_HUMAN : PlayerType.LOCAL_CPU;
    this.player2Type = player2.isHumanPlayer() ? PlayerType.LOCAL_HUMAN : PlayerType.LOCAL_CPU;
    this.listener = listener;
    this.viewModel=viewModel;
  }

  boolean started;

  @Override
  public void render(GameSnapshot gameSnapshot) {
    if (GameStatus.RUNNING == gameSnapshot.getStatus()) {
      if (!started) {
        started = true;
        viewModel.postMatchStart(new MatchStartMessage(player1Type, player2Type, null, null, Piece.PLAYER_1));
        //listener.onMatchStart();
      }
      //listener.onMatchPlayerMove(new MatchStatusMessage(null, null, gameSnapshot));
      viewModel.postMatchMove(new MatchStatusMessage(null, null, gameSnapshot));
    } else {
      // render final status
      //listener.onMatchPlayerMove(new MatchStatusMessage(null, null, gameSnapshot));
      viewModel.postMatchMove(new MatchStatusMessage(null, null, gameSnapshot));
      // propagate result
      viewModel.postMatchEnd(new MatchEndMessage(null, null, gameSnapshot.getStatus(), gameSnapshot.getScore()));
      //listener.onMatchEnd(new MatchEndMessage(null, null, gameSnapshot.getStatus(), gameSnapshot.getScore()));
    }
  }
}