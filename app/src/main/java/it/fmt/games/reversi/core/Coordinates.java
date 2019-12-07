package it.fmt.games.reversi.core;

public class Coordinates implements Comparable<Coordinates> {
    private int row;
    private int column;

    public Coordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Coordinates of(int row, int column) {
        return new Coordinates(row, column);
    }

    public static Coordinates of(String value) {
        String v = value.toUpperCase();
        if (value.length() == 0) {
            throw new InvalidCoordinatesException("Invalid coordinates!");
        }
        return new Coordinates(v.charAt(1) - '0' - 1, v.charAt(0) - 'A');
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isValid() {
        return (row >= 0 && row <= 7 && column >= 0 && column <= 7);
    }

    public Coordinates translate(Direction direction) {
        return translate(direction, 1);
    }

    public Coordinates translate(Direction direction, int count) {
        int newRow = direction.getOffsetRow() * count + row;
        int newCol = direction.getOffsetCol() * count + column;
        return Coordinates.of(newRow, newCol);
    }

    @Override
    public String toString() {
        return "" + ((char) ('A' + column)) + (row + 1);
    }

    @Override
    public int compareTo(Coordinates o) {
        return this.toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (row != that.row) return false;
        return column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }
}
