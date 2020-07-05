package it.fmt.games.reversi.android.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.android.databinding.FragmentMatchBinding;
import it.fmt.games.reversi.android.repositories.network.model.ErrorStatus;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageVisitor;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.fragments.support.FragmentUtils;
import it.fmt.games.reversi.android.ui.support.BoardDrawer;
import it.fmt.games.reversi.android.ui.support.AppDialogUtils;
import it.fmt.games.reversi.android.ui.support.CpuType;
import it.fmt.games.reversi.android.ui.support.GameContainerUtils;
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
  Drawable whitePieceDrawable;
  Drawable blackPieceDrawable;
  float rotationAngle;
  private FragmentMatchBinding binding;
  private MatchViewModel viewModel;
  private GameType gameType;
  private PlayerType player1Type;
  private PlayerType player2Type;
  private boolean alreadyShowError;

  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    binding = FragmentMatchBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();

    gameType = MatchFragmentArgs.fromBundle(getArguments()).getGameType();
    GameContainerUtils.definePieces(requireActivity(), this, gameType);
    GameContainerUtils.defineTagAndClickListeners(this);

    String playerName = null;
    CpuType cpuType = null;
    if (GameType.PLAYER_VS_NETWORK == gameType) {
      viewModel = new ViewModelProvider(this).get(NetworkMatchViewModel.class);
      playerName = PreferenceManager.getDefaultSharedPreferences(requireActivity())
              .getString("prefs_network_player_name", "Player");
    } else {
      viewModel = new ViewModelProvider(this).get(LocalMatchViewModel.class);
      cpuType = CpuType.valueOf(PreferenceManager.getDefaultSharedPreferences(requireActivity())
              .getString("prefs_local_cpu_type", CpuType.RANDOM.toString()));
    }

    FragmentUtils.configureHomeButton(requireActivity(), true);

    showWait();

    viewModel.onStartMessage().observe(getViewLifecycleOwner(), this::visit);
    viewModel.onStatusMessage().observe(getViewLifecycleOwner(), this::visit);
    viewModel.onEndMessage().observe(getViewLifecycleOwner(), this::visit);
    viewModel.onErrorStatus().observe(getViewLifecycleOwner(), this::onErrorStatus);
    viewModel.match(playerName, gameType, cpuType);

    setHasOptionsMenu(true);

    return view;
  }

  private void onErrorStatus(ErrorStatus errorStatus) {
    if (!alreadyShowError) {
      AppDialogUtils.showErrorDialog(this);
      alreadyShowError = true;
    }
  }

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
    GameContainerUtils.showPlayerScore(this, gameSnapshot.getScore());
    animatePlayerSelection();
    BoardDrawer.draw(this, gameSnapshot, gameType);
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
    if (v.getTag() != null) {
      Coordinates coordinate = (Coordinates) v.getTag();

      Timber.i("try to select %s", coordinate.toString());
      boolean validMove = viewModel.readUserMove(coordinate);
      if (validMove) {
        BoardDrawer.drawSelected(this, coordinate);
      }
    }
  }

  @Override
  public void visit(MatchStartMessage message) {
    player1Type = message.getPlayer1Type();
    player2Type = message.getPlayer2Type();
    GameContainerUtils.defineLabels(getActivity(), this, message);
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
    BoardDrawer.removeHints(this);
    if (status.isGameOver()) {
      AppDialogUtils.showResultDialog(this, status);
    }
  }
}