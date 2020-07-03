package it.fmt.games.reversi.android.repositories.model;

import org.jetbrains.annotations.NotNull;

import it.fmt.games.reversi.android.repositories.network.model.ErrorStatus;

public interface ErrorEventDispatcher {
  void postErrorStatus(@NotNull ErrorStatus errorStatus);
}
