package it.fmt.games.reversi.android.ui.support;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.ui.activities.GameActivity;

public class GameActivityHelper {
  public static void definePieces(GameActivity gameActivity, GameType gameType) {
    gameActivity.setWhitePieceDrawable(gameActivity.getDrawable(R.drawable.white_256));
    gameActivity.setBlackPieceDrawable(gameActivity.getDrawable(R.drawable.black_256));
    gameActivity.setRotationAngle(-90f);
  }

  public static void defineLabels(GameActivity gameActivity, String player1Label, String player2Label) {
    gameActivity.getTvPlayer1Title().setText(player1Label);
    gameActivity.getTvPlayer2Title().setText(player2Label);
  }
}
