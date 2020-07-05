package it.fmt.games.reversi.android.ui.support;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;
import it.fmt.games.reversi.android.ui.views.GridViewItem;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;

public abstract class BoardDrawer {
  public static void drawSelected(GameContainer gameContainer, Coordinates selectedCoords) {
    AppGridLayout appGridLayout = gameContainer.getAppGridLayout();
    GridViewItem view = (GridViewItem) appGridLayout.getChildAt(selectedCoords.getRow() * Board.BOARD_SIZE + selectedCoords.getColumn());
    view.setImageResource(R.drawable.hint_selected_256);
  }

  public static void draw(GameContainer gameContainer, GameSnapshot gameSnapshot, GameType gameType) {
    final boolean showHints = showHints(gameContainer, gameSnapshot.getActivePiece(), gameType);
    final List<Coordinates> availableMoves = gameSnapshot.getAvailableMoves().getMovesActivePlayer();
    final List<Coordinates> capturedMoves = gameSnapshot.getLastMove() != null ? gameSnapshot.getLastMove().getCapturedEnemyPiecesCoords() : new ArrayList<>();
    AppGridLayout appGridLayout = gameContainer.getAppGridLayout();
    Drawable whitePiece = gameContainer.getWhitePieceDrawable();
    Drawable blackPiece = gameContainer.getBlackPieceDrawable();

    gameSnapshot.getBoard().getCellStream().forEach(item -> {
      Coordinates coords = item.getCoordinates();
      GridViewItem view = (GridViewItem) appGridLayout.getChildAt(coords.getRow() * Board.BOARD_SIZE + coords.getColumn());
      // only in available moves we found the coords
      view.setTag(null);
      switch (item.getPiece()) {
        case EMPTY:
          if (showHints && availableMoves.indexOf(coords) >= 0) {
            view.setImageResource(R.drawable.hint_256);
            view.setTag(coords);
          } else {
            view.setImageResource(R.drawable.transparent);
          }
          break;
        case PLAYER_1:
          if (capturedMoves.indexOf(coords) >= 0) {
            AnimationUtils.animatePieceFlip(view, whitePiece, blackPiece, 500);
          } else {
            view.setImageDrawable(blackPiece);
          }
          break;
        case PLAYER_2:
          if (capturedMoves.indexOf(coords) >= 0) {
            AnimationUtils.animatePieceFlip(view, blackPiece, whitePiece, 500);
          } else {
            view.setImageDrawable(whitePiece);
          }
          break;
      }
    });
  }

  public static void removeHints(GameContainer container) {
    AppGridLayout appGridLayout = container.getAppGridLayout();
    for (int i = 0; i < appGridLayout.getChildCount(); i++) {
      GridViewItem view = (GridViewItem) appGridLayout.getChildAt(i);
      if (view.getTag() != null) {
        view.setTag(null);
        view.setImageResource(R.drawable.transparent);
      }
    }
  }

  private static boolean showHints(GameContainer container, Piece activePiece, GameType gameType) {
    switch (gameType) {
      case CPU_VS_CPU:
        return false;
      case PLAYER_VS_CPU:
        return activePiece == Piece.PLAYER_1;
      case CPU_VS_PLAYER:
        return activePiece == Piece.PLAYER_2;
      case PLAYER_VS_NETWORK:
        return (activePiece == Piece.PLAYER_1 && container.getPlayer1Type() != PlayerType.NETWORK_PLAYER) ||
                (activePiece == Piece.PLAYER_2 && container.getPlayer2Type() != PlayerType.NETWORK_PLAYER);
      case PLAYER_VS_PLAYER:
      default:
        return true;
    }
  }
}
