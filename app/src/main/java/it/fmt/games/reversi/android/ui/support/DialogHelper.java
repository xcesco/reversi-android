package it.fmt.games.reversi.android.ui.support;

import android.app.Activity;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import org.jetbrains.annotations.NotNull;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.ResultDialogBinding;
import it.fmt.games.reversi.android.ui.activities.MainActivity;
import it.fmt.games.reversi.model.GameStatus;

public abstract class DialogHelper {
  public static void showResultDialog(Activity context, GameStatus status) {
    @NotNull ResultDialogBinding binding = ResultDialogBinding.inflate(context.getLayoutInflater());
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
    (new AlertDialog.Builder(context))
            .setView(binding.getRoot())
            .setPositiveButton("Play another game", (dialog, which) -> {
              context.startActivity(MainActivity.createIntent(context));
              context.finish();
            })
            .setNegativeButton("Return to board", null).create().show();
  }
}
