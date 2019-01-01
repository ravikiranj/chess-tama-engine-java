package com.chesstama.backend.util;

import com.chesstama.backend.engine.Board;
import com.chesstama.backend.engine.Position;

public final class BoardUtil {
    private BoardUtil() {
    }

    public static int get1DBoardPosition(final int piecePosition) {
        // Find the rightmost set bit using (N & ~(N-1))
        int pos = BitMathUtil.log2(piecePosition & ~(piecePosition - 1));
        return Board.BOARD_INDEX_MAX - pos;
    }

    public static Position get2DBoardPosition(final int pos) {
        return new Position(pos / Board.MAX_ROWS, pos % Board.MAX_COLS);
    }
}
