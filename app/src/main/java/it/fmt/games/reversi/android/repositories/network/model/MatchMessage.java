package it.fmt.games.reversi.android.repositories.network.model;

import java.util.UUID;

public abstract class MatchMessage implements MatchMessageVisitable {
  private final UUID playerId;
  private final UUID matchId;
  private final MatchMessageType messageType;

  public MatchMessage(UUID playerId, UUID matchId, MatchMessageType messageType) {
    this.playerId = playerId;
    this.matchId = matchId;
    this.messageType = messageType;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public MatchMessageType getMessageType() {
    return messageType;
  }

  public UUID getMatchId() {
    return matchId;
  }

  @Override
  public void accept(MatchMessageVisitor visitor) {
    visitor.visit(this);
  }

}
