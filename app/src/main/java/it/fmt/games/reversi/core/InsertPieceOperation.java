package it.fmt.games.reversi.core;

import java.util.List;

public class InsertPieceOperation {
    private final Board board;
    private final Piece pieceToInsert;
    private final List<Coordinates> capturedPiecesCoordinates;
    private final Coordinates insertionCoordinate;

    public InsertPieceOperation(Board board, Coordinates coords, Piece pieceToInsert, List<Coordinates> capturedPiecesCoordinates) {
        this.pieceToInsert = pieceToInsert;
        this.board = board;
        this.insertionCoordinate = coords;
        this.capturedPiecesCoordinates = capturedPiecesCoordinates;
        if (pieceToInsert == Piece.EMPTY) throw (new InvalidPieceSelectedException());
    }

    public Board apply() {
        board.setCell(insertionCoordinate, pieceToInsert);
        capturedPiecesCoordinates.stream().forEach(item -> board.setCell(item, pieceToInsert));
        return board;
    }
}
