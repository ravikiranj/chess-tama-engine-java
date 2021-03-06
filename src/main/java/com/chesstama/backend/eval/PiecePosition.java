package com.chesstama.backend.eval;

import com.chesstama.backend.engine.PieceType;
import com.chesstama.backend.engine.Position;

import java.util.Objects;

public class PiecePosition {
    private final PieceType pieceType;
    private final Position position;

    public PiecePosition(final PieceType pieceType,
                         final Position position) {
        this.pieceType = pieceType;
        this.position = position;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "PieceMove{" +
                "pieceType=" + pieceType +
                ", position=" + position +
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

        PiecePosition that = (PiecePosition) o;
        return pieceType == that.pieceType &&
            position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, position);
    }
}
