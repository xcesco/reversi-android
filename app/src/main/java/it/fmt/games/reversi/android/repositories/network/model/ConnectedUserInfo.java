package it.fmt.games.reversi.android.repositories.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ConnectedUserInfo implements Serializable {
    @JsonCreator
    public ConnectedUserInfo(@JsonProperty("name") String name) {
        this.name = name;
    }

    public static ConnectedUserInfo of(String name) {
        return new ConnectedUserInfo(name);
    }

    public String getName() {
        return name;
    }

    private final String name;
}
