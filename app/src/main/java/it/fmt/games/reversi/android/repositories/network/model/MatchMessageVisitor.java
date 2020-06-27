package it.fmt.games.reversi.android.repositories.network.model;


import java.util.concurrent.ExecutionException;

public interface MatchMessageVisitor {
  void visit(MatchStartMessage message);

  void visit(MatchStatusMessage message);

  void visit(MatchEndMessage message);

  MatchEndMessage getMatchEndMessage() throws ExecutionException, InterruptedException;
}