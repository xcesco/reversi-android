package it.fmt.games.reversi.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.android.logic.GameLogicThread;
import it.fmt.games.reversi.android.ui.AppGridLayout;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Score;


public class GameActivity extends AppCompatActivity implements GameRenderer, View.OnClickListener {

    public static final String GAME_TYPE = "game_type";

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

    Drawable whitePieceDrawable;

    Drawable blackPieceDrawable;

    Player1 player1;

    Player2 player2;

    float rotationAngle;

    public static Intent createIntent(Context context, String gameType) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(GAME_TYPE, gameType);
        return intent;
    }

    public Player1 getPlayer1() {
        return player1;
    }

    public Player2 getPlayer2() {
        return player2;
    }

    public AppGridLayout getAppGridLayout() {
        return appGridLayout;
    }

    public Drawable getWhitePieceDrawable() {
        return whitePieceDrawable;
    }

    public Drawable getBlackPieceDrawable() {
        return blackPieceDrawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        GameActivityHelper.definePlayersAndPieces(this);

        gameLogic = new GameLogicThread(this, player1, player2, this);
        gameLogic.start();
    }


    @Override
    public void render(GameSnapshot gameSnapshot) {
        showPlayerScore(gameSnapshot.getScore());

        animatePlayerSelection();

        BoardAndroidDrawer.draw(this, gameSnapshot);
        GameStatus status = gameSnapshot.getStatus();

        if (status.isGameOver()) {
            DialogHelper.showResultDialog(this, status);
        }
    }

    private void showPlayerScore(Score score) {
        this.blackNum.setText("" + score.getPlayer1Score());
        this.whiteNum.setText("" + score.getPlayer2Score());
    }

    private void animatePlayerSelection() {
        ivPlayerSelector.animate().setDuration(600).rotation(rotationAngle).start();
        rotationAngle *= -1;
    }

    @Override
    public void onClick(View v) {
        Coordinates coordinate = (Coordinates) v.getTag();

        synchronized (gameLogic.getAcceptedMove()) {
            gameLogic.getAcceptedMove().setCoordinates(coordinate);
            gameLogic.getAcceptedMove().notifyAll();
        }
    }
}
