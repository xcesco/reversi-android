package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchMove {

  private final UUID playerId;
  private final Piece playerPiece;
  private final Coordinates move;
  private final UUID matchId;

  @JsonCreator
  public MatchMove(@JsonProperty("matchUUID") UUID matchId,
                   @JsonProperty("playerUUID") UUID playerId,
                   @JsonProperty("playerPiece") Piece playerPiece,
                   @JsonProperty("move") Coordinates move) {
    this.playerId = playerId;
    this.playerPiece = playerPiece;
    this.move = move;
    this.matchId = matchId;
  }

  public static MatchMove of(UUID matchId, UUID playerId, Piece playerPiece, Coordinates move) {
    return new MatchMove(matchId, playerId, playerPiece, move);
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public Piece getPlayerPiece() {
    return playerPiece;
  }

  public Coordinates getMove() {
    return move;
  }

  public UUID getMatchId() {
    return matchId;
  }
}
