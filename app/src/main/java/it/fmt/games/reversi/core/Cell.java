package it.fmt.games.reversi.core;

import java.util.Objects;

public class Cell {

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Piece getPiece() {
        return piece;
    }

    private final Coordinates coordinates;
    private final Piece piece;

    public Cell(Coordinates c, Piece p) {
        this.coordinates = c;
        this.piece = p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (!Objects.equals(coordinates, cell.coordinates)) return false;
        return piece == cell.piece;
    }

    @Override
    public int hashCode() {
        int result = coordinates != null ? coordinates.hashCode() : 0;
        result = 31 * result + (piece != null ? piece.hashCode() : 0);
        return result;
    }
}
