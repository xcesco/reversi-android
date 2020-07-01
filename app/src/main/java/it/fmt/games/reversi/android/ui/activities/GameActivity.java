package it.fmt.games.reversi.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.android.databinding.ActivityGameBinding;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitor;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.support.BoardAndroidDrawer;
import it.fmt.games.reversi.android.ui.support.DialogHelper;
import it.fmt.games.reversi.android.ui.support.GameActivityHelper;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;
import it.fmt.games.reversi.android.viewmodels.LocalMatchViewModel;
import it.fmt.games.reversi.android.viewmodels.MatchViewModel;
import it.fmt.games.reversi.android.viewmodels.NetworkMatchViewModel;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import timber.log.Timber;


public class GameActivity extends AppCompatActivity implements MatchMessageVisitor, GameRenderer, View.OnClickListener {

  private void showIndicator() {
    binding.pbWait.setVisibility(View.INVISIBLE);
    binding.tvWait.setVisibility(View.INVISIBLE);
    binding.ivPlayerSelector.setVisibility(View.VISIBLE);
  }

  private void showWait() {
    binding.pbWait.setVisibility(View.VISIBLE);
    binding.tvWait.setVisibility(View.VISIBLE);
    binding.ivPlayerSelector.setVisibility(View.INVISIBLE);
  }

  public static final String GAME_TYPE = "game_type";
  public ActivityGameBinding binding;
  Drawable whitePieceDrawable;
  Drawable blackPieceDrawable;
  float rotationAngle;
  private MatchViewModel viewModel;
  private GameType gameType;
  private PlayerType player1Type;
  private PlayerType player2Type;

  public static Intent createIntent(Context context, GameType gameType) {
    Intent intent = new Intent(context, GameActivity.class);
    intent.putExtra(GAME_TYPE, gameType.toString());
    return intent;
  }

  public PlayerType getPlayer1Type() {
    return player1Type;
  }

  public PlayerType getPlayer2Type() {
    return player2Type;
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

    String gameTypeValue = getIntent().getStringExtra(GAME_TYPE);
    gameType = GameType.valueOf(gameTypeValue);
    GameActivityHelper.definePieces(this, gameType);

    if (GameType.PLAYER_VS_NETWORK == gameType) {
      viewModel = new ViewModelProvider(this).get(NetworkMatchViewModel.class);
    } else {
      viewModel = new ViewModelProvider(this).get(LocalMatchViewModel.class);
    }

    showWait();

    viewModel.onStartMessage().observe(this, this::visit);
    viewModel.onStatusMessage().observe(this, this::visit);
    viewModel.onEndMessage().observe(this, this::visit);

    viewModel.match(this, gameType);
  }

  @Override
  public void render(GameSnapshot gameSnapshot) {
    GameActivityHelper.showPlayerScore(this, gameSnapshot.getScore());
    animatePlayerSelection();
    BoardAndroidDrawer.draw(this, gameSnapshot, gameType);
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

    viewModel.readUserMove(coordinate);
  }

  @Override
  public void visit(MatchStartMessage message) {
    player1Type = message.getPlayer1Type();
    player2Type = message.getPlayer2Type();
    GameActivityHelper.defineLabels(this, message.getPlayer1Type(), message.getPlayer2Type());
    showIndicator();
  }

  @Override
  public void visit(MatchStatusMessage message) {
    Timber.i(message.getMessageType().toString());
    render(message.getGameSnapshot());
  }

  @Override
  public void visit(MatchEndMessage message) {
    GameStatus status = message.getStatus();
    Timber.i("finish %s - %s", message.getMessageType().toString(), status);

    if (status.isGameOver() && !this.isFinishing()) {
      DialogHelper.showResultDialog(this, status);
    }
  }

}
