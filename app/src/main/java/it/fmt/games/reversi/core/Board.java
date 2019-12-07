package it.fmt.games.reversi.core;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board {
    public static final int BOARD_SIZE = 8;
    final Cell[] cells;

    public Board() {
        this.cells = new Cell[BOARD_SIZE * BOARD_SIZE];
        IntStream.range(0, BOARD_SIZE)
                .forEach(row -> IntStream.range(0, BOARD_SIZE)
                        .forEach(col -> setCell(Coordinates.of(row, col), Piece.EMPTY)));
    }

    public Board(Cell[] cells) {
        this.cells = cells;
    }

    public Stream<Cell> getCellStream() {
        return Arrays.stream(cells);
    }

    public void setCell(Coordinates coordinates, Piece content) {
        if (!coordinates.isValid()) throw new InvalidCoordinatesException("Invalid coordinates!");
        cells[coordinates.getRow() * BOARD_SIZE + coordinates.getColumn()] = new Cell(coordinates, content);
    }

    public Piece getCellContent(Coordinates coordinates) {
        if (!coordinates.isValid()) throw new InvalidCoordinatesException("Invalid coordinates!");
        return cells[coordinates.getRow() * BOARD_SIZE + coordinates.getColumn()].getPiece();
    }

    public boolean isCellContentEqualsTo(Coordinates coordinates, Piece currentPlayer) {
        return coordinates.isValid() && getCellContent(coordinates) == currentPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;
        return Arrays.equals(cells, board.cells);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cells);
    }
}


