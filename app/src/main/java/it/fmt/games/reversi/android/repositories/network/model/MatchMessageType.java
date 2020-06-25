package it.fmt.games.reversi.android.repositories.network.model;

public enum MatchMessageType {
  MATCH_START(MatchStartMessage.class),
  MATCH_STATUS(MatchStatusMessage.class),
  MATCH_END(MatchEndMessage.class);

  public Class<? extends MatchMessage> getType() {
    return type;
  }

  private final Class<? extends MatchMessage> type;

  MatchMessageType(Class<? extends MatchMessage> type) {
    this.type = type;
  }
}
