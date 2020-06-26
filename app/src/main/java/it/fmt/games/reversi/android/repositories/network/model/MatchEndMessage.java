package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchEndMessage extends MatchMessage {

  @JsonCreator
  public MatchEndMessage(@JsonProperty("playerId") UUID playerId, @JsonProperty("matchId") UUID matchId) {
    super(playerId, matchId, MatchMessageType.MATCH_END);
  }

  @Override
  public void accept(MatchMessageVisitor visitor) {
    visitor.visit(this);
  }

}
