package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchStartMessage extends MatchMessage {
  private final Piece assignedPiece;
  private final PlayerType player1Type;
  private final PlayerType player2Type;
  private final String player1Name;
  private final String player2Name;

  @JsonCreator
  public MatchStartMessage(@JsonProperty("player1Type") PlayerType player1Type, @JsonProperty("player1Name") String player1Name,
                           @JsonProperty("player2Type") PlayerType player2Type, @JsonProperty("player2Name") String player2Name,
                           @JsonProperty("playerId") UUID playerId, @JsonProperty("matchId") UUID matchId,
                           @JsonProperty("piece") Piece assignedPiece) {
    super(playerId, matchId, MatchMessageType.MATCH_START);
    this.assignedPiece = assignedPiece;
    this.player1Type = player1Type;
    this.player2Type = player2Type;
    this.player1Name = player1Name;
    this.player2Name = player2Name;
  }

  public String getPlayer1Name() {
    return player1Name;
  }

  public String getPlayer2Name() {
    return player2Name;
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
