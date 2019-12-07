package it.fmt.games.reversi.core;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnemyPiecesToCaptureFinder {
    private final Coordinates searchOrigin;
    private final Piece piece;
    private final Board board;
    private final Piece enemyPiece;

    public EnemyPiecesToCaptureFinder(Board board, Coordinates coords, Piece piece) {
        this.piece = piece;
        this.board = board;
        this.searchOrigin = coords;
        this.enemyPiece = piece == Piece.PLAYER_1 ? Piece.PLAYER_2 : Piece.PLAYER_1;
        if (piece == Piece.EMPTY) throw (new InvalidPieceSelectedException());
    }

    public List<Coordinates> find() {
        return Stream.of(Direction.values()).filter(this::isAnyPieceToInvertAlongDirection)
                .map(this::rotateEnemyPieces).flatMap(x -> x).sorted()
                .collect(Collectors.toList());
    }

    private boolean isAnyPieceToInvertAlongDirection(Direction direction) {
        return isAnyPieceToInvert(searchOrigin, direction);
    }

    private Stream<Coordinates> rotateEnemyPieces(Direction direction) {
        return findEnemyPiecesAlongDirection(searchOrigin, direction);
    }

    private boolean isAnyPieceToInvert(Coordinates initialCoordinates, Direction direction) {
        int piecesToReverte = (int) findEnemyPiecesAlongDirection(initialCoordinates, direction).count();
        return piecesToReverte > 0 &&
                board.isCellContentEqualsTo(initialCoordinates.translate(direction, piecesToReverte + 1), piece);
    }

    private Stream<Coordinates> findEnemyPiecesAlongDirection(Coordinates coordinates, Direction direction) {
        return Stream.iterate(coordinates.translate(direction),
                coords -> board.isCellContentEqualsTo(coords, enemyPiece),
                coords -> coords.translate(direction));
    }
}