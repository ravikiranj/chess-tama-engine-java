package com.chesstama.engine;

import java.util.List;

@SuppressWarnings("PMD")
public class Evaluator {
    public Evaluator() {
    }

    public long getBoardValue(final Board board) {
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer == Player.P1) {
            // King
            Position kingPos = board.getKingPosition(currentPlayer);


            // Pawns
            List<Position> pawnPositions = board.getPawnPositions(currentPlayer);
        }

        return 0;
    }
}
