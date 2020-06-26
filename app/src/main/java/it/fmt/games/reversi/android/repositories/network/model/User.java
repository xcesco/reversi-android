package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import it.fmt.games.reversi.model.Piece;

public class User {
  private final String name;

  public UserStatus getStatus() {
    return status;
  }

  private final UserStatus status;

  public Piece getPiece() {
    return piece;
  }

  private final Piece piece;

  public String getName() {
    return name;
  }

  public UUID getId() {
    return id;
  }

  private final UUID id;

  @JsonCreator
  public User(@JsonProperty("id") UUID id, @JsonProperty("name") String name, @JsonProperty("status") UserStatus status, @JsonProperty("piece") Piece piece) {
    this.id = id;
    this.name = name;

    this.status = status;
    this.piece = piece;
  }
}
