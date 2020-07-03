package it.fmt.games.reversi.android.ui.support;

import android.content.Context;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.google.android.gms.common.util.Strings;

import java.util.stream.IntStream;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;
import it.fmt.games.reversi.model.Score;

public class GameContainerUtils {
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

  public static void defineLabels(Context context, GameContainer container, MatchStartMessage message) {
    String text1 = resolveText(context, Piece.PLAYER_1, message.getPlayer1Name(), message.getPlayer1Type());
    String text2 = resolveText(context, Piece.PLAYER_2, message.getPlayer2Name(), message.getPlayer2Type());
    container.setPlayer1Title(text1);
    container.setPlayer2Title(text2);
  }

  public static void showPlayerScore(GameContainer container, Score score) {
    container.setPlayer1Score("" + score.getPlayer1Score());
    container.setPlayer2Score("" + score.getPlayer2Score());
  }

  private static String resolveText(Context context, Piece player, String name, PlayerType playerType) {
    if (!Strings.isEmptyOrWhitespace(name)) return name;

    String playerName1 = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("prefs_local_player1_name", context.getResources().getString(R.string.default_player1_name));
    String playerName2 = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("prefs_local_player2_name", context.getResources().getString(R.string.default_player2_name));

    int res;
    switch (playerType) {
      case LOCAL_CPU:
        res = Piece.PLAYER_1 == player ? R.string.match_player1_cpu : R.string.match_player2_cpu;
        return context.getResources().getString(res);
      case NETWORK_PLAYER:
        res = R.string.player_network;
        return context.getResources().getString(res);
      case HUMAN_PLAYER:
      default:
        return Piece.PLAYER_1 == player ? playerName1 : playerName2;
    }
  }


}
