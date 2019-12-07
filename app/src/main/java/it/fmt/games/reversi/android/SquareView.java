package it.fmt.games.reversi.android;

import android.content.Context;

import androidx.appcompat.view.ContextThemeWrapper;

public class SquareView extends androidx.appcompat.widget.AppCompatImageView {

    public SquareView(Context context, int row, int column){
        super(new ContextThemeWrapper(context, (row +column)% 2==0 ? R.style.AppGridItemDark: R.style.AppGridItemLight),null, 0);

    }

}