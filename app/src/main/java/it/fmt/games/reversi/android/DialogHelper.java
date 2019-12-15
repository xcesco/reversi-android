package it.fmt.games.reversi.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import it.fmt.games.reversi.model.GameStatus;

public abstract class DialogHelper {
    public static void showResultDialog(Activity context, GameStatus status) {
        AlertDialog.Builder builder = (new AlertDialog.Builder(context));
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.result_dialog, null);
        builder.setView(dialogView);

        ImageView imageview= dialogView.findViewById(R.id.dialog_image_view);

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
        builder.setPositiveButton("Play again", (dialog, which) -> {
            context.startActivity(MainActivity.createIntent(context));
            context.finish();
        })
                .setNegativeButton("OK", null).show();
    }
}
