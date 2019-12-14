package it.fmt.games.reversi.android;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.PlayerFactory;
import it.fmt.games.reversi.android.logic.AndroidPlayer;
import it.fmt.games.reversi.android.logic.GameLogicThread;
import it.fmt.games.reversi.android.ui.AppGridLayout;
import it.fmt.games.reversi.android.ui.GridViewItem;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;


public class GameActivity extends AppCompatActivity implements GameRenderer, View.OnClickListener {

    private static final String GAME_TYPE = "game_type";

    @BindView(R.id.blackNum)
    TextView blackNum;

    @BindView(R.id.whiteNum)
    TextView whiteNum;

    @BindView(R.id.gridLayout)
    AppGridLayout appGridLayout;

    private Coordinates coordinate;
    private GameLogicThread gameLogic;
    private Drawable whitePiece;
    private Drawable blackPiece;
    private Player1 player1;
    private Player2 player2;


    public static Intent createIntent(Context context, String gameType) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(GAME_TYPE, gameType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        whitePiece = getDrawable(R.drawable.white_256);
        blackPiece = this.getDrawable(R.drawable.black_256);

        String type=getIntent().getStringExtra(GAME_TYPE);

        switch (type) {
            case "1":
                player1 = PlayerFactory.createUserPlayer1();
                player2 = PlayerFactory.createUserPlayer2();
                break;
            case "2":
                player1 = PlayerFactory.createUserPlayer1();
                player2 = PlayerFactory.createRoboPlayer2(new AndroidPlayer());
                break;
            case "3":
                player1 = PlayerFactory.createRoboPlayer1(new AndroidPlayer());
                player2 = PlayerFactory.createUserPlayer2();
                break;
        }

        gameLogic = new GameLogicThread(this, player1, player2, this);
        gameLogic.start();
    }

    private boolean showHints(Piece activePiece) {
        if (player1.isHumanPlayer() && activePiece == player1.getPiece()) return true;
        if (player2.isHumanPlayer() && activePiece == player2.getPiece()) return true;

        return false;
    }

    @Override
    public void render(GameSnapshot gameSnapshot) {
        Log.i("RENDER", "draw");
        final boolean showHints = showHints(gameSnapshot.getActivePiece());
        this.blackNum.setText(MessageFormat.format("BLACK: {0}", gameSnapshot.getScore().getPlayer1Score()));
        this.whiteNum.setText(MessageFormat.format("WHITE: {0}", gameSnapshot.getScore().getPlayer2Score()));
        final List<Coordinates> availableMoves = gameSnapshot.getAvailableMoves().getMovesActivePlayer();
        final List<Coordinates> capturedMoves = gameSnapshot.getLastMove() != null ? gameSnapshot.getLastMove().getCapturedEnemyPiecesCoords() : new ArrayList<>();

        gameSnapshot.getBoard().getCellStream().forEach(item -> {
            GridViewItem view = (GridViewItem) appGridLayout.getChildAt(item.getCoordinates().getRow() * Board.BOARD_SIZE + item.getCoordinates().getColumn());
            view.setOnClickListener(GameActivity.this);
            view.setTag(item.getCoordinates());
            switch (item.getPiece()) {
                case EMPTY:
                    if (showHints && availableMoves.indexOf(item.getCoordinates()) >= 0) {
                        view.setImageResource(R.drawable.hint_256);
                    } else {
                        view.setImageResource(R.drawable.transparent);
                    }
                    break;
                case PLAYER_1:
                    if (capturedMoves.indexOf(item.getCoordinates()) >= 0) {
                        changeImage(view, whitePiece, blackPiece, 500);
                    } else {
                        view.setImageDrawable(blackPiece);
                    }
                    break;
                case PLAYER_2:
                    if (capturedMoves.indexOf(item.getCoordinates()) >= 0) {
                        changeImage(view, blackPiece, whitePiece, 500);
                    } else {
                        view.setImageDrawable(whitePiece);
                    }
                    break;
            }
        });

        if (gameSnapshot.getStatus().isGameOver()) {
            (new AlertDialog.Builder(this)).setMessage(gameSnapshot.getStatus().toString()).show();
        }
    }

    private void changeImage(GridViewItem view, Drawable startPiece, Drawable endkPiece, int animDuration) {
        view.setImageDrawable(startPiece);
        view.setScaleY(1.0f);
        view.animate().setDuration(animDuration / 2).scaleY(0.f).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setImageDrawable(endkPiece);
                view.animate().setDuration(animDuration / 2).scaleY(1.0f).setListener(null);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        coordinate = (Coordinates) v.getTag();

        synchronized (gameLogic.acceptedMove) {
            gameLogic.acceptedMove.setCoordinates(coordinate);
            gameLogic.acceptedMove.notifyAll();
        }
    }
}
