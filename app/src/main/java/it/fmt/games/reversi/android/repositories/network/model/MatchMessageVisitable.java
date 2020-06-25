package it.fmt.games.reversi.android.repositories.network.model;

public interface MatchMessageVisitable {
  void accept(MatchMessageVisitor visitor);
}