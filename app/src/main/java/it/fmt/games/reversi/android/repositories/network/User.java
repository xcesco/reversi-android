package it.fmt.games.reversi.android.repositories.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private final String name;
    private final UserStatus status;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    private final String id;

    @JsonCreator
    public User(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("status") UserStatus status) {
        this.id=id;
        this.name=name;

        this.status = status;
    }
}
