package it.fmt.games.reversi.android.repositories.network.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MatchMessageVisitorImpl implements MatchMessageVisitor {
  public MatchMessageVisitorImpl() {

  }

  private final CompletableFuture<MatchEndMessage> matchEndMessageCompletableFuture = new CompletableFuture<>();

  @Override
  public void visit(MatchStartMessage message) {

  }

  @Override
  public void visit(MatchStatusMessage message) {

  }

  @Override
  public void visit(MatchEndMessage message) {
    matchEndMessageCompletableFuture.complete(message);
  }

  public MatchEndMessage getMatchEndMessage() throws ExecutionException, InterruptedException {
    return matchEndMessageCompletableFuture.get();
  }

}
