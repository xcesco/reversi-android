package it.fmt.games.reversi.android.repositories.network.model;


public interface MatchMessageVisitor {
  void visit(MatchStartMessage message);

  void visit(MatchStatusMessage message);

  void visit(MatchEndMessage message);
}