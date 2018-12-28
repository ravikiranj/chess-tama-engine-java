package com.chesstama.testers;

import com.chesstama.engine.Board;
import com.chesstama.engine.PieceType;
import com.chesstama.engine.Player;
import com.chesstama.engine.Position;
import com.chesstama.eval.Move;

public class MoveTester {
    public static void main(final String[] args) {
        Board board = new Board.Builder().build();
        board.printBoardState();
        board.printBoardOnly();
        System.out.println("===========================");
        System.out.println("===========================");

        Player currentPlayer = board.getCurrentPlayer();
        Move move = new Move(
                currentPlayer,
                board.getCards(currentPlayer).get(0),
                PieceType.KING,
                Board.P1_KING_SLOT,
                new Position(3, 3)
        );

        board.makeMove(move);
        board.printBoardOnly();
    }
}
