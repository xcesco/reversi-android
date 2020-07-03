package it.fmt.games.reversi.android.viewmodels.model;

import it.fmt.games.reversi.model.Coordinates;

public class Move {
    public Coordinates getCoordinates() {
        return coordinates;
    }

    private Coordinates coordinates;

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}