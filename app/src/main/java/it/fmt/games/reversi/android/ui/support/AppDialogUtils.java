package it.fmt.games.reversi.android.ui.support;

import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.ResultDialogBinding;
import it.fmt.games.reversi.android.ui.fragments.MatchFragment;
import it.fmt.games.reversi.model.GameStatus;

public abstract class AppDialogUtils {
  public static void showResultDialog(MatchFragment fragment, GameStatus status) {
    FragmentActivity activity = fragment.requireActivity();
    @NotNull ResultDialogBinding binding = ResultDialogBinding.inflate(activity.getLayoutInflater());
    ImageView imageview = binding.dialogImageView;

    switch (status) {
      case DRAW:
        imageview.setImageResource(R.drawable.win_draw512);
        break;
      case PLAYER1_WIN:
        imageview.setImageResource(R.drawable.win_1_512);
        break;
      case PLAYER2_WIN:
        imageview.setImageResource(R.drawable.win_2_512);
        break;

    }
    (new AlertDialog.Builder(activity))
            .setView(binding.getRoot())
            .setPositiveButton(R.string.play_another_game, (dialog, which) -> {
              activity.onBackPressed();
            })
            .setNegativeButton(R.string.back_to_board, null).create().show();
  }

  public static void showErrorDialog(MatchFragment fragment) {
    FragmentActivity activity = fragment.requireActivity();

    (new AlertDialog.Builder(activity))
            .setIcon(R.drawable.ic_warning_24)
            .setTitle(R.string.dialgo_error_title)
            .setMessage(R.string.dialog_error_message)
            .setPositiveButton(R.string.OK, (dialog, which) -> {
              activity.onBackPressed();
            })
            .create().show();
  }
}
