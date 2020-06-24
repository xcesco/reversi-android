package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchMove {

  @JsonCreator
  public MatchMove(@JsonProperty("matchUUID") UUID matchUUID,
                   @JsonProperty("playerUUID") UUID playerUUID,
                   @JsonProperty("playerPiece") Piece playerPiece,
                   @JsonProperty("move") Coordinates move) {
    this.playerUUID = playerUUID;
    this.playerPiece = playerPiece;
    this.move = move;
    this.matchUUID = matchUUID;
  }

  private final UUID playerUUID;

  public UUID getPlayerUUID() {
    return playerUUID;
  }

  public Piece getPlayerPiece() {
    return playerPiece;
  }

  public Coordinates getMove() {
    return move;
  }

  public UUID getMatchUUID() {
    return matchUUID;
  }

  private final Piece playerPiece;
  private final Coordinates move;
  private final UUID matchUUID;

  public static MatchMove of(UUID matchUUID, UUID playerUUID, Piece playerPiece, Coordinates move) {
    return new MatchMove(matchUUID, playerUUID, playerPiece, move);
  }
}
