package it.fmt.games.reversi.android.ui.support;

import android.content.Context;
import android.view.View;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.activities.GameActivity;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;
import it.fmt.games.reversi.model.Score;

public class GameActivityHelper {
  public static void definePieces(Context context, GameContainer container, GameType gameType) {
    container.setWhitePieceDrawable(context.getDrawable(R.drawable.white_256));
    container.setBlackPieceDrawable(context.getDrawable(R.drawable.black_256));
    container.setRotationAngle(-90f);
  }

  public static void defineTagAndClickListeners(GameContainer gameContainer) {
    AppGridLayout appGridLayout = gameContainer.getAppGridLayout();

    IntStream.range(0, Board.BOARD_SIZE * Board.BOARD_SIZE).forEach(index ->
    {
      Coordinates coords = Coordinates.of(index % Board.BOARD_SIZE, index / Board.BOARD_SIZE);
      View view = appGridLayout.getChildAt(coords.getRow() * Board.BOARD_SIZE + coords.getColumn());
      view.setOnClickListener(gameContainer);
      view.setTag(coords);
    });
  }

  public static void defineLabels(GameActivity activity, PlayerType player1Type, PlayerType player2Type) {
    int text1 = resolveText(Piece.PLAYER_1, player1Type);
    int text2 = resolveText(Piece.PLAYER_2, player2Type);
    activity.binding.player1Title.setText(text1);
    activity.binding.player2Title.setText(text2);
  }

  public static void showPlayerScore(GameContainer container, Score score) {
    container.setPlayer1Score("" + score.getPlayer1Score());
    container.setPlayer2Score("" + score.getPlayer2Score());
  }

  private static int resolveText(Piece player, PlayerType playerType) {
    switch (playerType) {

      case LOCAL_CPU:
        return Piece.PLAYER_1 == player ? R.string.player1_cpu : R.string.player2_cpu;
      case NETWORK_PLAYER:
        return R.string.player_network;
      case HUMAN_PLAYER:
      default:
        return Piece.PLAYER_1 == player ? R.string.player1_human : R.string.player2_human;

    }
  }
}
