package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ConnectedUser implements Serializable {
    @JsonCreator
    public ConnectedUser(@JsonProperty("name") String name) {
        this.name = name;
    }

    public static ConnectedUser of(String name) {
        return new ConnectedUser(name);
    }

    public String getName() {
        return name;
    }

    private final String name;
}
