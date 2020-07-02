package it.fmt.games.reversi.android.ui.adapters;

import android.widget.ImageView;

public interface ItemSelectionListener<E> {
    void onSelect(int position, E item, ImageView imageView, int selectionType);
}
