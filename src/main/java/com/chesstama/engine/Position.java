package com.chesstama.engine;

import java.util.Objects;

public class Position {
    private final int row;
    private final int col;

    public Position(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position negate() {
        return new Position(-1 * row, -1 * col);
    }

    public Position add(final Position position) {
        return new Position(row + position.getRow(), col + position.getCol());
    }

    public boolean isValid() {
        return row >= Board.MIN_ROW_INDEX && row <= Board.MAX_ROW_INDEX
                && col >= Board.MIN_COL_INDEX && col <= Board.MAX_COL_INDEX;
    }

    @Override
    public String toString() {
        return "Position{" +
            "row=" + row +
            ", col=" + col +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return row == position.row &&
            col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
