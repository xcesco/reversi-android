package it.fmt.games.reversi.android;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;


public class GameActivity extends AppCompatActivity implements GameRenderer, View.OnClickListener {

    private static final String GAME_TYPE = "game_type";

    @BindView(R.id.ivPlayerSelector)
    ImageView ivPlayerSelector;

    @BindView(R.id.tvPlayer1Title)
    TextView tvPlayer1Title;

    @BindView(R.id.tvPlayer2Title)
    TextView tvPlayer2Title;

    @BindView(R.id.tvPlayer1Score)
    TextView blackNum;

    @BindView(R.id.tvPlayer2Score)
    TextView whiteNum;

    @BindView(R.id.gridLayout)
    AppGridLayout appGridLayout;

    private GameLogicThread gameLogic;
    private Drawable whitePiece;
    private Drawable blackPiece;
    private Player1 player1;
    private Player2 player2;
    private float rotationAngle;


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
        rotationAngle = -90f;

        String type = getIntent().getStringExtra(GAME_TYPE);

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
            case "4":
                player1 = PlayerFactory.createRoboPlayer1(new AndroidPlayer());
                player2 = PlayerFactory.createRoboPlayer2(new AndroidPlayer());
                break;
        }
        if (!player1.isHumanPlayer()) tvPlayer1Title.setText("CPU");
        if (!player2.isHumanPlayer()) tvPlayer2Title.setText("CPU");

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
        this.blackNum.setText("" + gameSnapshot.getScore().getPlayer1Score());
        this.whiteNum.setText("" + gameSnapshot.getScore().getPlayer2Score());
        final List<Coordinates> availableMoves = gameSnapshot.getAvailableMoves().getMovesActivePlayer();
        final List<Coordinates> capturedMoves = gameSnapshot.getLastMove() != null ? gameSnapshot.getLastMove().getCapturedEnemyPiecesCoords() : new ArrayList<>();

        animatePlayerSelection();

        gameSnapshot.getBoard().getCellStream().forEach(item -> {
            Coordinates coords = item.getCoordinates();
            GridViewItem view = (GridViewItem) appGridLayout.getChildAt(coords.getRow() * Board.BOARD_SIZE + coords.getColumn());
            view.setOnClickListener(GameActivity.this);
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
                        animatePieceFlip(view, whitePiece, blackPiece, 500);
                    } else {
                        view.setImageDrawable(blackPiece);
                    }
                    break;
                case PLAYER_2:
                    if (capturedMoves.indexOf(coords) >= 0) {
                        animatePieceFlip(view, blackPiece, whitePiece, 500);
                    } else {
                        view.setImageDrawable(whitePiece);
                    }
                    break;
            }
        });

        GameStatus status = gameSnapshot.getStatus();
        if (status.isGameOver()) {
            String msg = "";
            switch (status) {
                case DRAW:
                    msg = String.format("Game draw!");
                    break;
                case PLAYER1_WIN:
                    msg = String.format("Player 1 wins %s to %s", gameSnapshot.getScore().getPlayer1Score(), gameSnapshot.getScore().getPlayer2Score());
                    break;
                case PLAYER2_WIN:
                    msg = String.format("Player 2 wins %s to %s", gameSnapshot.getScore().getPlayer2Score(), gameSnapshot.getScore().getPlayer1Score());
                    break;

            }

            AlertDialog.Builder builder = (new AlertDialog.Builder(this));
            builder.setPositiveButton("Play again", (dialog, which) -> {
                GameActivity.this.startActivity(MainActivity.createIntent(GameActivity.this));
                GameActivity.this.finish();
            })
                    .setNegativeButton("OK", null)
                    .setTitle(R.string.title_end).setMessage(msg).show();
        }
    }

    private void animatePlayerSelection() {
        ivPlayerSelector.animate().setDuration(600).rotation(rotationAngle).start();
        rotationAngle *= -1;
    }

    private void animatePieceFlip(GridViewItem view, Drawable startPiece, Drawable endkPiece, int animDuration) {
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
                //view.setImageDrawable(middlePiece);

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
        Coordinates coordinate = (Coordinates) v.getTag();

        synchronized (gameLogic.acceptedMove) {
            gameLogic.acceptedMove.setCoordinates(coordinate);
            gameLogic.acceptedMove.notifyAll();
        }
    }
}
