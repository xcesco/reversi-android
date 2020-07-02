package it.fmt.games.reversi.android.ui.adapters;

import it.fmt.games.reversi.android.databinding.PlayedMatchItemBinding;

public class PlayedMatchViewHolder extends CustomViewHolder {
  final public PlayedMatchItemBinding binding;

  public PlayedMatchViewHolder(PlayedMatchItemBinding binding) {
    super(binding.getRoot());
    this.binding = binding;
  }
}
