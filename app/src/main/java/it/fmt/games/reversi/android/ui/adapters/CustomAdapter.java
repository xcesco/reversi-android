package it.fmt.games.reversi.android.ui.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class CustomAdapter<E, VH extends CustomViewHolder> extends RecyclerView.Adapter<VH> {

  protected final ItemSelectionListener<E> listener;
  protected Context mContext;
  protected List<E> list;


  public CustomAdapter(@NonNull Context mContext, @NonNull List<E> list, ItemSelectionListener<E> listener) {
    this.mContext = mContext;
    this.list = list;
    this.listener = listener;

    this.setHasStableIds(true);
  }

  public void updateValues(List<E> value) {
    list.clear();
    if (value != null && value.size() > 0) {
      list.addAll(value);
    }
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return list == null ? 0 : list.size();
  }

  public void clearData() {
    list.clear();
    notifyDataSetChanged();
  }
  
  @Override
  public long getItemId(int position) {
    return position;
  }

  public E getItemByPosition(long key) {
    int index = (int) key;
    if (index < list.size()) {
      return list.get(index);
    }
    return null;
  }

}
