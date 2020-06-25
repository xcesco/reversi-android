package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class User {
  private final String name;
  private final UserStatus status;

  public String getName() {
    return name;
  }

  public UUID getId() {
    return id;
  }

  private final UUID id;

  @JsonCreator
  public User(@JsonProperty("id") UUID id, @JsonProperty("name") String name, @JsonProperty("status") UserStatus status) {
    this.id = id;
    this.name = name;

    this.status = status;
  }
}
