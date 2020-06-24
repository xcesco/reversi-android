package it.fmt.games.reversi.android.repositories.persistence;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.fmt.games.reversi.android.repositories.network.NetworkClient;

@Singleton
public class MatchRepository {

    private final NetworkClient networkClient;

    @Inject
    public MatchRepository(NetworkClient networkClient) {
        this.networkClient=networkClient;
    }
}
