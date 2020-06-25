package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchStartMessage extends MatchMessage {
  private final Piece assignedPiece;

  @JsonCreator
  public MatchStartMessage(@JsonProperty("playerId") UUID playerId, @JsonProperty("matchId") UUID matchId, @JsonProperty("piece") Piece assignedPiece) {
    super(playerId, matchId, MatchMessageType.MATCH_START);
    this.assignedPiece = assignedPiece;
  }

  public Piece getAssignedPiece() {
    return assignedPiece;
  }
}
