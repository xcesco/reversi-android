package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Score;

import java.util.UUID;

public class MatchEndMessage extends MatchMessage {

  private final GameStatus status;
  private final Score score;

  @JsonCreator
  public MatchEndMessage(@JsonProperty("playerId") UUID playerId, @JsonProperty("matchId") UUID matchId, @JsonProperty("status") GameStatus status, @JsonProperty("score") Score score) {
    super(playerId, matchId, MatchMessageType.MATCH_END);
    this.status = status;
    this.score = score;
  }

  public GameStatus getStatus() {
    return status;
  }

  public Score getScore() {
    return score;
  }

  @Override
  public void accept(MatchMessageVisitor visitor) {
    visitor.visit(this);
  }
}
