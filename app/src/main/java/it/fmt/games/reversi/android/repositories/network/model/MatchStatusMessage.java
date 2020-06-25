package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.GameSnapshot;

import java.util.UUID;

public class MatchStatusMessage extends MatchMessage {
  private final GameSnapshot gameSnapshot;

  @JsonCreator
  public MatchStatusMessage(@JsonProperty("playerId") UUID playerId, @JsonProperty("matchId") UUID matchId, @JsonProperty("gameSnapshot") GameSnapshot gameSnapshot) {
    super(playerId, matchId, MatchMessageType.MATCH_STATUS);
    this.gameSnapshot = gameSnapshot;
  }

  public GameSnapshot getGameSnapshot() {
    return gameSnapshot;
  }
}