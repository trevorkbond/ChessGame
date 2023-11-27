package chess;

import java.util.Objects;

public class ChessPositionImpl implements ChessPosition {
    private int row;
    private int column;

    public ChessPositionImpl(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public ChessPosition getVerticalMove(int rowMove) {
        if (((row + rowMove) > 8) || (((row + rowMove) < 1))) {
            return null;
        } else {
            return new ChessPositionImpl(row + rowMove, column);
        }
    }

    public ChessPosition getMixedMove(int rowMove, int colMove) {
        if ((((row + rowMove) > 8) || (((row + rowMove) < 1))) || (((column + colMove) > 8) || (((column + colMove) < 1)))) {
            return null;
        } else {
            return new ChessPositionImpl(row + rowMove, column + colMove);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPositionImpl that)) return false;
        return getRow() == that.getRow() && getColumn() == that.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", row, column);
    }
}
