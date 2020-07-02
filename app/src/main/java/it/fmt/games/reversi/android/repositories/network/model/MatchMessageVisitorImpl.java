package it.fmt.games.reversi.android.repositories.network.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

public class MatchMessageVisitorImpl implements MatchMessageVisitor {
  public MatchMessageVisitorImpl() {

  }

  private final CompletableFuture<MatchEndMessage> matchEndMessageCompletableFuture = new CompletableFuture<>();

  @Override
  public void visit(MatchStartMessage message) {
    Timber.i("received MatchStartMessage");
  }

  @Override
  public void visit(MatchStatusMessage message) {
    Timber.i("received MatchStatusMessage");
  }

  @Override
  public void visit(MatchEndMessage message) {
    Timber.i("received MatchEndMessage");
    matchEndMessageCompletableFuture.complete(message);
  }

  public MatchEndMessage getMatchEndMessage() throws ExecutionException, InterruptedException {
    return matchEndMessageCompletableFuture.get();
  }

}
