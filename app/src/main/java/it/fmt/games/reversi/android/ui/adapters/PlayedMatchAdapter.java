package it.fmt.games.reversi.android.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.PlayedMatchItemBinding;
import it.fmt.games.reversi.android.repositories.persistence.database.PlayedMatch;

public class PlayedMatchAdapter extends CustomAdapter<PlayedMatch, PlayedMatchViewHolder> {

  private static DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ITALY);

  public PlayedMatchAdapter(Context mContext, ItemSelectionListener<PlayedMatch> listener) {
    super(mContext, new ArrayList<>(), listener);
  }

  @NonNull
  @Override
  public PlayedMatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final PlayedMatchViewHolder holder = new PlayedMatchViewHolder(PlayedMatchItemBinding.inflate(LayoutInflater.from(parent.getContext()),
            parent, false));

    return holder;
  }

  @Override
  public void onBindViewHolder(final PlayedMatchViewHolder holder, int position) {
    PlayedMatch item = list.get(position);

    holder.binding.playedMatchPlayer1.setText(item.player1);
    holder.binding.playedMatchPlayer2.setText(item.player2);
    holder.binding.playedMatchDate.setText(dateFormat.format(item.date));
    holder.binding.playedMatchImg.setImageResource(item.winner ? R.drawable.winner : R.drawable.looser);
    holder.binding.playedMatchP1Score.setText(""+item.player1Score);
    holder.binding.playedMatchP2Score.setText(""+item.player2Score);
    holder.binding.playedMatchStatus.setText(item.gameStatus.toString().replace("_", " "));
  }

}