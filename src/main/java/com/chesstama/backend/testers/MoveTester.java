package com.chesstama.backend.testers;

import com.chesstama.backend.engine.Board;
import com.chesstama.backend.engine.Card;
import com.chesstama.backend.engine.PieceType;
import com.chesstama.backend.engine.Player;
import com.chesstama.backend.engine.Position;
import com.chesstama.backend.eval.Move;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 +============+============+============+============+============+====+====+====+====+====+====+====+
 | 0 - (0,0)  | 1 - (0,1)  | 2 - (0,2)  | 3 - (0,3)  | 4 - (0,4)  |    |    |    |    |    |    |    |
 +============+============+============+============+============+====+====+====+====+====+====+====+
 | 5 - (1,0)  | 6 - (1,1)  | 7 - (1,2)  | 8 - (1,3)  | 9 - (1,4)  |    |    |    |    |    |    |    |
 +------------+------------+------------+------------+------------+----+----+----+----+----+----+----+
 | 10 - (2,0) | 11 - (2,1) | 12 - (2,2) | 13 - (2,3) | 14 - (2,4) |    |    |    |    |    |    |    |
 +------------+------------+------------+------------+------------+----+----+----+----+----+----+----+
 | 15 - (3,0) | 16 - (3,1) | 17 - (3,2) | 18 - (3,3) | 19 - (3,4) |    |    |    |    |    |    |    |
 +------------+------------+------------+------------+------------+----+----+----+----+----+----+----+
 | 20 - (4,0) | 21 - (4,1) | 22 - (4,2) | 23 - (4,3) | 24 - (4,4) | 25 | 26 | 27 | 28 | 29 | 30 | 31 |
 +------------+------------+------------+------------+------------+----+----+----+----+----+----+----+
 */
@Slf4j
public class MoveTester {
    public static void main(final String[] args) {
        Board board1 = new Board.Builder().build();
        board1.printBoardState();
        board1.printBoardOnly();
        playMatch1(board1);

        Board board2 = new Board.Builder().build();
        board2.printBoardState();
        board2.printBoardOnly();
        playMatch2(board2);
    }

    private static void playMatch2(final Board board) {
         // Move 1
        Move move1 = new Move(
                Player.P1,
                Card.MONKEY,
                PieceType.PAWN,
                new Position(4, 1),
                new Position(3, 2)
        );

        // Move 2
        Move move2 = new Move(
                Player.P2,
                Card.DRAGON,
                PieceType.PAWN,
                new Position(0, 1),
                new Position(1, 3)
        );

        // Move 3
        Move move3 = new Move(
                Player.P1,
                Card.TIGER,
                PieceType.PAWN,
                new Position(3, 2),
                new Position(1, 2)
        );

        // Move 4
        Move move4 = new Move(
                Player.P2,
                Card.MANTIS,
                PieceType.PAWN,
                new Position(0, 3),
                new Position(1, 2)
        );

        List<Move> moves = new ImmutableList.Builder<Move>()
                .add(move1)
                .add(move2)
                .add(move3)
                .add(move4)
                .build();

        makeMoves(board, moves);
    }

    private static void playMatch1(final Board board) {
        // Move 1
        Move move1 = new Move(
                Player.P1,
                Card.MONKEY,
                PieceType.KING,
                Board.P1_KING_SLOT,
                new Position(3, 3)
        );

        // Move 2
        Move move2 = new Move(
                Player.P2,
                Card.DRAGON,
                PieceType.KING,
                Board.P2_KING_SLOT,
                new Position(1, 4)
        );

        // Move 3
        Move move3 = new Move(
                Player.P1,
                Card.TIGER,
                PieceType.KING,
                new Position(3, 3),
                new Position(1, 3)
        );

        // Move 4
        Move move4 = new Move(
                Player.P2,
                Card.MANTIS,
                PieceType.KING,
                new Position(1, 4),
                new Position(2, 3)
        );

        // Move 5
        Move move5 = new Move(
                Player.P1,
                Card.ELEPHANT,
                PieceType.KING,
                new Position(1, 3),
                new Position(1, 2)
        );

        // Move 6
        Move move6 = new Move(
                Player.P2,
                Card.MONKEY,
                PieceType.KING,
                new Position(2, 3),
                new Position(1, 2)
        );

        List<Move> moves = new ImmutableList.Builder<Move>()
                .add(move1)
                .add(move2)
                .add(move3)
                .add(move4)
                .add(move5)
                .add(move6)
                .build();

        makeMoves(board, moves);
        log.info("Game Over = {}, Game Winner = {}", board.isGameOver(), board.getGameWinner());
    }

    public static void makeMoves(final Board board,
                                 final List<Move> moves) {
        int count = 1;
        for (Move move : moves) {
            System.out.println("===========================");
            System.out.println("===========================");
            log.info("Making move {} = {}", count++, move);
            Player currentPlayer = board.getCurrentPlayer();
            Player opponent = currentPlayer.getOpponent();
            log.info("Cards = {}, UpcomingCard = {}", board.getCards(currentPlayer), board.getUpcomingCard(currentPlayer));
            log.info("Opp Cards = {}, Opp UpcomingCard = {}", board.getCards(opponent), board.getUpcomingCard(opponent));
            board.makeMove(move);
            board.printBoardOnly();
        }
    }
}
