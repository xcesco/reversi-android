package it.fmt.games.reversi.android.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.android.databinding.ActivityGameBinding;
import it.fmt.games.reversi.android.logic.GameViewModel;
import it.fmt.games.reversi.android.ui.support.BoardAndroidDrawer;
import it.fmt.games.reversi.android.ui.support.DialogHelper;
import it.fmt.games.reversi.android.ui.support.GameActivityHelper;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Score;


public class GameActivity extends AppCompatActivity implements GameRenderer, View.OnClickListener {

  public static final String GAME_TYPE = "game_type";

  private ActivityGameBinding binding;

//    @BindView(R.id.ivPlayerSelector)
//    ImageView ivPlayerSelector;
//
//    @BindView(R.id.tvPlayer1Title)
//    TextView tvPlayer1Title;
//
//    @BindView(R.id.tvPlayer2Title)
//    TextView tvPlayer2Title;
//
//    @BindView(R.id.tvPlayer1Score)
//    TextView blackNum;
//
//    @BindView(R.id.tvPlayer2Score)
//    TextView whiteNum;
//
//    @BindView(R.id.gridLayout)
//    AppGridLayout appGridLayout;

  Drawable whitePieceDrawable;

  Drawable blackPieceDrawable;

  Player1 player1;

  Player2 player2;

  float rotationAngle;

  private GameViewModel viewModel;

  public static Intent createIntent(Context context, String gameType) {
    Intent intent = new Intent(context, GameActivity.class);
    intent.putExtra(GAME_TYPE, gameType);
    return intent;
  }

  public TextView getTvPlayer1Title() {
    return binding.tvPlayer1Title;
  }

  public TextView getTvPlayer2Title() {
    return binding.tvPlayer2Title;
  }

  public Player1 getPlayer1() {
    return player1;
  }

  public void setPlayer1(Player1 player1) {
    this.player1 = player1;
  }

  public Player2 getPlayer2() {
    return player2;
  }

  public void setPlayer2(Player2 player2) {
    this.player2 = player2;
  }

  public AppGridLayout getAppGridLayout() {
    return binding.gridLayout;
  }

  public Drawable getWhitePieceDrawable() {
    return whitePieceDrawable;
  }

  public void setWhitePieceDrawable(Drawable whitePieceDrawable) {
    this.whitePieceDrawable = whitePieceDrawable;
  }

  public Drawable getBlackPieceDrawable() {
    return blackPieceDrawable;
  }

  public void setBlackPieceDrawable(Drawable blackPieceDrawable) {
    this.blackPieceDrawable = blackPieceDrawable;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityGameBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    GameActivityHelper.definePlayersAndPieces(this);

    viewModel = new ViewModelProvider(this).get(GameViewModel.class);
    viewModel.play(this, player1, player2, this).observe(this, finalGameSnapshot -> {
      GameStatus status = finalGameSnapshot.getStatus();
      if (status.isGameOver() && !this.isFinishing()) {
        DialogHelper.showResultDialog(this, status);
      }
    });
  }


  @Override
  public void render(GameSnapshot gameSnapshot) {
    showPlayerScore(gameSnapshot.getScore());
    animatePlayerSelection();
    BoardAndroidDrawer.draw(this, gameSnapshot);
  }

  @SuppressLint("SetTextI18n")
  private void showPlayerScore(Score score) {
    binding.tvPlayer1Score.setText("" + score.getPlayer1Score());
    binding.tvPlayer2Score.setText("" + score.getPlayer2Score());
  }

  public void setRotationAngle(float rotationAngle) {
    this.rotationAngle = rotationAngle;
  }

  private void animatePlayerSelection() {
    binding.ivPlayerSelector.animate().setDuration(600).rotation(rotationAngle).start();
    rotationAngle *= -1;
  }

  @Override
  public void onClick(View v) {
    Coordinates coordinate = (Coordinates) v.getTag();

    synchronized (viewModel.getAcceptedMove()) {
      viewModel.getAcceptedMove().setCoordinates(coordinate);
      viewModel.getAcceptedMove().notifyAll();
    }
  }
}
