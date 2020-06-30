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
import it.fmt.games.reversi.android.databinding.ActivityGameBinding;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitor;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.ui.support.BoardAndroidDrawer;
import it.fmt.games.reversi.android.ui.support.DialogHelper;
import it.fmt.games.reversi.android.ui.support.GameActivityHelper;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;
import it.fmt.games.reversi.android.viewmodels.LocalMatchViewModel;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Score;
import timber.log.Timber;


public class GameActivity extends AppCompatActivity implements MatchMessageVisitor, GameRenderer, View.OnClickListener {

  public static final String GAME_TYPE = "game_type";
  Drawable whitePieceDrawable;
  Drawable blackPieceDrawable;
  float rotationAngle;
  public ActivityGameBinding binding;
  private LocalMatchViewModel viewModel;
  private GameType gameType;

  public static Intent createIntent(Context context, GameType gameType) {
    Intent intent = new Intent(context, GameActivity.class);
    intent.putExtra(GAME_TYPE, gameType.toString());
    return intent;
  }

  public TextView getTvPlayer2Title() {
    return binding.player2Title;
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

    viewModel = new ViewModelProvider(this).get(LocalMatchViewModel.class);

    viewModel.onStartMessageLiveData().observe(this, this::visit);
    viewModel.onStatusMessageLiveData().observe(this, this::visit);
    viewModel.onEndMessageLiveData().observe(this, this::visit);

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
    GameActivityHelper.defineLabels(this, message.getPlayer1Type(), message.getPlayer2Type());
  }

  @Override
  public void visit(MatchStatusMessage message) {
    Timber.i(message.getMessageType().toString());
    render(message.getGameSnapshot());
  }

  @Override
  public void visit(MatchEndMessage message) {
    Timber.i(message.getMessageType().toString());
    GameStatus status = message.getStatus();

    if (status.isGameOver() && !this.isFinishing()) {
      DialogHelper.showResultDialog(this, status);
    }
  }

}
