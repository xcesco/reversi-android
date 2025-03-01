package it.fmt.games.reversi.android.ui.support;

import android.animation.Animator;
import android.graphics.drawable.Drawable;

import it.fmt.games.reversi.android.ui.views.GridViewItem;

public abstract class AnimationUtils {
    public static void animatePieceFlip(GridViewItem view, Drawable startPiece, Drawable endkPiece, int animDuration) {
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
                view.setImageDrawable(endkPiece);
                view.animate().setDuration(animDuration / 2).scaleY(1.0f).setListener(null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
    }
}
