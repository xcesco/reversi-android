package it.fmt.games.reversi.android.ui.fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import java.util.Objects;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.android.databinding.FragmentMatchBinding;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitor;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.fragments.support.FragmentUtils;
import it.fmt.games.reversi.android.ui.support.BoardAndroidDrawer;
import it.fmt.games.reversi.android.ui.support.DialogHelper;
import it.fmt.games.reversi.android.ui.support.GameActivityHelper;
import it.fmt.games.reversi.android.ui.support.GameContainer;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;
import it.fmt.games.reversi.android.viewmodels.LocalMatchViewModel;
import it.fmt.games.reversi.android.viewmodels.MatchViewModel;
import it.fmt.games.reversi.android.viewmodels.NetworkMatchViewModel;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import timber.log.Timber;

public class MatchFragment extends Fragment implements MatchMessageVisitor, GameRenderer, GameContainer {

  public static final String GAME_TYPE = "game_type";
  Drawable whitePieceDrawable;
  Drawable blackPieceDrawable;
  float rotationAngle;
  private FragmentMatchBinding binding;
  private MatchViewModel viewModel;
  private GameType gameType;
  private PlayerType player1Type;
  private PlayerType player2Type;

  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    binding = FragmentMatchBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();

    String gameTypeValue = null; //getIntent().getStringExtra(GAME_TYPE);
    gameType = GameType.valueOf(gameTypeValue);
    GameActivityHelper.definePieces(requireActivity(), this, gameType);
    GameActivityHelper.defineTagAndClickListeners(this);

    if (GameType.PLAYER_VS_NETWORK == gameType) {
      viewModel = new ViewModelProvider(this).get(NetworkMatchViewModel.class);
    } else {
      viewModel = new ViewModelProvider(this).get(LocalMatchViewModel.class);
    }

    FragmentUtils.configureHomeButton(requireActivity(), true);

    showWait();
    String playerName = PreferenceManager.getDefaultSharedPreferences(requireActivity())
            .getString("userName", "Player");

    viewModel.onStartMessage().observe(getViewLifecycleOwner(), this::visit);
    viewModel.onStatusMessage().observe(getViewLifecycleOwner(), this::visit);
    viewModel.onEndMessage().observe(getViewLifecycleOwner(), this::visit);
    viewModel.match(playerName, gameType);

    setHasOptionsMenu(true);

    return view;
  }

//  @Override
//  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//    menu.clear();
//    super.onCreateOptionsMenu(menu, inflater);
//  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

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

  @Override
  public PlayerType getPlayer1Type() {
    return player1Type;
  }

  @Override
  public PlayerType getPlayer2Type() {
    return player2Type;
  }

  @Override
  public AppGridLayout getAppGridLayout() {
    return binding.gridLayout;
  }

  @Override
  public Drawable getWhitePieceDrawable() {
    return whitePieceDrawable;
  }

  @Override
  public void setWhitePieceDrawable(Drawable whitePieceDrawable) {
    this.whitePieceDrawable = whitePieceDrawable;
  }

  @Override
  public Drawable getBlackPieceDrawable() {
    return blackPieceDrawable;
  }

  @Override
  public void setBlackPieceDrawable(Drawable blackPieceDrawable) {
    this.blackPieceDrawable = blackPieceDrawable;
  }

  @Override
  public void render(GameSnapshot gameSnapshot) {
    GameActivityHelper.showPlayerScore(this, gameSnapshot.getScore());
    animatePlayerSelection();
    BoardAndroidDrawer.draw(this, gameSnapshot, gameType);
  }

  @Override
  public void setRotationAngle(float rotationAngle) {
    this.rotationAngle = rotationAngle;
  }

  @Override
  public void setPlayer1Title(String value) {
    binding.player1Title.setText(value);
  }

  @Override
  public void setPlayer2Title(String value) {
    binding.player2Title.setText(value);
  }

  @Override
  public void setPlayer1Score(String value) {
    binding.tvPlayer1Score.setText(value);
  }

  @Override
  public void setPlayer2Score(String value) {
    binding.tvPlayer2Score.setText(value);
  }

  private void animatePlayerSelection() {
    binding.ivPlayerSelector.animate().setDuration(600).rotation(rotationAngle).start();
    rotationAngle *= -1;
  }

  @Override
  public void onClick(View v) {
    Coordinates coordinate = (Coordinates) v.getTag();

    Timber.i("try to select %s", coordinate.toString());
    boolean validMove = viewModel.readUserMove(coordinate);
    if (validMove) {
      BoardAndroidDrawer.drawSelected(this, coordinate);
    }
  }

  @Override
  public void visit(MatchStartMessage message) {
    player1Type = message.getPlayer1Type();
    player2Type = message.getPlayer2Type();
    GameActivityHelper.defineLabels(getActivity(), this, message);
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

    if (status.isGameOver()) {
      DialogHelper.showResultDialog(getActivity(), status);
    }
  }
}