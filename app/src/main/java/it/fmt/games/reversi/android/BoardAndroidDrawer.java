package it.fmt.games.reversi.android;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import it.fmt.games.reversi.android.ui.AppGridLayout;
import it.fmt.games.reversi.android.ui.GridViewItem;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;

public abstract class BoardAndroidDrawer {
    public static void draw(GameActivity gameActivity, GameSnapshot gameSnapshot) {
        final boolean showHints = showHints(gameActivity, gameSnapshot.getActivePiece());
        final List<Coordinates> availableMoves = gameSnapshot.getAvailableMoves().getMovesActivePlayer();
        final List<Coordinates> capturedMoves = gameSnapshot.getLastMove() != null ? gameSnapshot.getLastMove().getCapturedEnemyPiecesCoords() : new ArrayList<>();
        AppGridLayout appGridLayout = gameActivity.getAppGridLayout();
        Drawable whitePiece = gameActivity.getWhitePieceDrawable();
        Drawable blackPiece = gameActivity.getBlackPieceDrawable();
        
        gameSnapshot.getBoard().getCellStream().forEach(item -> {
            Coordinates coords = item.getCoordinates();
            GridViewItem view = (GridViewItem) appGridLayout.getChildAt(coords.getRow() * Board.BOARD_SIZE + coords.getColumn());
            view.setOnClickListener(gameActivity);
            view.setTag(coords);
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

    private static boolean showHints(GameActivity gameActivity, Piece activePiece) {
        if (gameActivity.getPlayer1().isHumanPlayer() && activePiece == gameActivity.getPlayer1().getPiece()) return true;
        if (gameActivity.getPlayer2().isHumanPlayer() && activePiece == gameActivity.getPlayer2().getPiece()) return true;

        return false;
    }
}
