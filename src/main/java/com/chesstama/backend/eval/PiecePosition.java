package com.chesstama.backend.eval;

import com.chesstama.backend.engine.PieceType;
import com.chesstama.backend.engine.Position;

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
}
