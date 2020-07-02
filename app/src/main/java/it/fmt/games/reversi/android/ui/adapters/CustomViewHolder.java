package it.fmt.games.reversi.android.ui.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public abstract class CustomViewHolder extends RecyclerView.ViewHolder {
  protected long previousClickTime;

  public CustomViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  public void bind(int value, boolean isActivated) {
    itemView.setActivated(isActivated);
  }
}