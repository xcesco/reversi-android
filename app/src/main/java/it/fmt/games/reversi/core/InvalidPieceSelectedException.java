package it.fmt.games.reversi.core;

public class InvalidPieceSelectedException extends RuntimeException {
    public InvalidPieceSelectedException() {
        super("Invalid piece selected");
    }
}
