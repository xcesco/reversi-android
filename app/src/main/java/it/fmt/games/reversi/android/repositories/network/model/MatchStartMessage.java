package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchStartMessage extends MatchMessage {
  private final Piece assignedPiece;
  private final PlayerType player1Type;
  private final PlayerType player2Type;

  @JsonCreator
  public MatchStartMessage(@JsonProperty("player1Type") PlayerType player1Type, @JsonProperty("player2Type") PlayerType player2Type, @JsonProperty("playerId") UUID playerId, @JsonProperty("matchId") UUID matchId, @JsonProperty("piece") Piece assignedPiece) {
    super(playerId, matchId, MatchMessageType.MATCH_START);
    this.assignedPiece = assignedPiece;
    this.player1Type = player1Type;
    this.player2Type = player2Type;
  }

  public PlayerType getPlayer1Type() {
    return player1Type;
  }

  public PlayerType getPlayer2Type() {
    return player2Type;
  }

  @Override
  public void accept(MatchMessageVisitor visitor) {
    visitor.visit(this);
  }

  public Piece getAssignedPiece() {
    return assignedPiece;
  }
}
