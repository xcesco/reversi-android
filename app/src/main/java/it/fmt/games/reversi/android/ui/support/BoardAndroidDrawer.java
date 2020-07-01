package it.fmt.games.reversi.android.ui.support;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.activities.GameActivity;
import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;
import it.fmt.games.reversi.android.ui.views.GridViewItem;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;

public abstract class BoardAndroidDrawer {
  public static void drawSelected(GameActivity gameActivity, Coordinates selectedCoords) {
    AppGridLayout appGridLayout = gameActivity.getAppGridLayout();
    GridViewItem view = (GridViewItem) appGridLayout.getChildAt(selectedCoords.getRow() * Board.BOARD_SIZE + selectedCoords.getColumn());
    view.setImageResource(R.drawable.hint_selected_256);
  }

  public static void draw(GameActivity gameActivity, GameSnapshot gameSnapshot, GameType gameType) {
    final boolean showHints = showHints(gameActivity, gameSnapshot.getActivePiece(), gameType);
    final List<Coordinates> availableMoves = gameSnapshot.getAvailableMoves().getMovesActivePlayer();
    final List<Coordinates> capturedMoves = gameSnapshot.getLastMove() != null ? gameSnapshot.getLastMove().getCapturedEnemyPiecesCoords() : new ArrayList<>();
    AppGridLayout appGridLayout = gameActivity.getAppGridLayout();
    Drawable whitePiece = gameActivity.getWhitePieceDrawable();
    Drawable blackPiece = gameActivity.getBlackPieceDrawable();

    gameSnapshot.getBoard().getCellStream().forEach(item -> {
      Coordinates coords = item.getCoordinates();
      GridViewItem view = (GridViewItem) appGridLayout.getChildAt(coords.getRow() * Board.BOARD_SIZE + coords.getColumn());
      switch (item.getPiece()) {
        case EMPTY:
          if (showHints && availableMoves.indexOf(coords) >= 0) {
            view.setImageResource(R.drawable.hint_256);
          } else {
            view.setImageResource(R.drawable.transparent);
          }
          break;
        case PLAYER_1:
          if (capturedMoves.indexOf(coords) >= 0) {
            AnimationHelper.animatePieceFlip(view, whitePiece, blackPiece, 500);
          } else {
            view.setImageDrawable(blackPiece);
          }
          break;
        case PLAYER_2:
          if (capturedMoves.indexOf(coords) >= 0) {
            AnimationHelper.animatePieceFlip(view, blackPiece, whitePiece, 500);
          } else {
            view.setImageDrawable(whitePiece);
          }
          break;
      }
    });
  }

  private static boolean showHints(GameActivity gameActivity, Piece activePiece, GameType gameType) {
    switch (gameType) {
      case CPU_VS_CPU:
        return false;
      case PLAYER_VS_CPU:
        return activePiece == Piece.PLAYER_1;
      case CPU_VS_PLAYER:
        return activePiece == Piece.PLAYER_2;
      case PLAYER_VS_NETWORK:
        return (activePiece == Piece.PLAYER_1 && gameActivity.getPlayer1Type() != PlayerType.NETWORK_PLAYER) ||
                (activePiece == Piece.PLAYER_2 && gameActivity.getPlayer2Type() != PlayerType.NETWORK_PLAYER);
      case PLAYER_VS_PLAYER:
      default:
        return true;
    }
  }
}
