package it.fmt.games.reversi.android.ui.support;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.activities.GameActivity;
import it.fmt.games.reversi.model.Score;

public class GameActivityHelper {
  public static void definePieces(GameActivity activity, GameType gameType) {
    activity.setWhitePieceDrawable(activity.getDrawable(R.drawable.white_256));
    activity.setBlackPieceDrawable(activity.getDrawable(R.drawable.black_256));
    activity.setRotationAngle(-90f);
  }

  public static void defineLabels(GameActivity activity, PlayerType player1Type, PlayerType player2Type) {
    int text1 = PlayerType.HUMAN_PLAYER == player1Type ? R.string.player1_human : R.string.player1_cpu;
    int text2 = PlayerType.HUMAN_PLAYER == player2Type ? R.string.player2_human : R.string.player2_cpu;
    activity.binding.player1Title.setText(text1);
    activity.binding.player2Title.setText(text2);
  }

  public static void showPlayerScore(GameActivity activity, Score score) {
    activity.binding.tvPlayer1Score.setText("" + score.getPlayer1Score());
    activity.binding.tvPlayer2Score.setText("" + score.getPlayer2Score());

  }
}
