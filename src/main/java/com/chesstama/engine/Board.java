package com.chesstama.engine;

import com.chesstama.bitmath.BitMath;

import java.util.ArrayList;
import java.util.List;

public class Board {
    /*
(0-7) | (8-15) | (16-23) | (24-31)
                 P2
Col     0    1    2    3    4
Row  +----+----+----+----+----+
 0   |  0 |  1 |  2 |  3 |  4 |
     +----+----+----+----+----+
 1   |  5 |  6 |  7 |  8 |  9 |
     +----+----+----+----+----+
 2   | 10 | 11 | 12 | 13 | 14 |
     +----+----+----+----+----+
 3   | 15 | 16 | 17 | 18 | 19 |
     +----+----+----+----+----+
 4   | 20 | 21 | 22 | 23 | 24 |
     +----+----+----+----+----+
                 P1
    */

    private static final int BOARD_INDEX_MAX = 31;
    private static final int MAX_ROWS = 5;
    private static final int MAX_COLS = 5;
    private static final int MAX_PAWNS = 4;

    // 00 | 00 | 02 | 00
    private final int p1King;

    // 00 | 00 | 0D | 80
    private final int p1Pawns;

    // 20 | 00 | 00 | 00
    private final int p2King;

    // D8 | 00 | 00 | 00
    private final int p2Pawns;

    public Board() {
        p1King = 0x00000200;
        p1Pawns = 0x00000D80;
        p2King = 0x20000000;
        p2Pawns = 0xD8000000;
    }

    public Position getKingPosition(PlayerType playerType) {
        int kingPosition = playerType == PlayerType.P1 ? p1King : p2King;
        int oneDimensionBoardPos = get1DBoardPosition(kingPosition);
        return get2DBoardPosition(oneDimensionBoardPos);
    }

    private int get1DBoardPosition(int piecePosition) {
        // Find the rightmost set bit using (N & ~(N-1))
        int pos = BitMath.log2(piecePosition & ~(piecePosition - 1));
        return BOARD_INDEX_MAX - pos;
    }

    private Position get2DBoardPosition(int pos) {
        return new Position(pos / MAX_ROWS, pos % MAX_COLS);
    }

    public List<Position> getPawnPositions(PlayerType playerType) {
        List<Position> result = new ArrayList<>(MAX_PAWNS);
        int pawnPosition = playerType == PlayerType.P1 ? p1Pawns : p2Pawns;
        int pawnsFound = 0;

        while (pawnPosition > 0 && pawnsFound < MAX_PAWNS) {
            // Extract rightmost set bit
            int pos = get1DBoardPosition(pawnPosition);

            // Add position to result
            result.add(get2DBoardPosition(pos));

            // Unset bit at pos
            pawnPosition = pawnPosition & ~(1 << (BOARD_INDEX_MAX - pos));

            pawnsFound++;
        }

        return result;
    }

    @Override
    public String toString() {
        return "Board{" +
            "p1King=" + p1King +
            ", p1Pawns=" + p1Pawns +
            ", p2King=" + p2King +
            ", p2Pawns=" + p2Pawns +
            '}';
    }
}
