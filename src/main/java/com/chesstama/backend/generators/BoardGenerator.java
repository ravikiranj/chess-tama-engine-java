package com.chesstama.backend.generators;

import com.chesstama.backend.engine.Board;
import com.chesstama.backend.engine.Card;
import com.chesstama.backend.engine.Player;
import com.chesstama.backend.eval.ScoreMoves;
import com.chesstama.backend.testers.MiniMaxTester;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class BoardGenerator {
    public static final String ET = ".";

    public static final String K1 = "K1";
    public static final String P1 = "P1";
    public static final String K2 = "K2";
    public static final String P2 = "P2";

    private static final Set<String> VALID_TOKENS = ImmutableSet.of(K1, P1, K2, P2, ET);

    private static final int MIN_KINGS = 1;
    private static final int MAX_PAWNS = 4;

    public static void main(final String[] args) {
        final String[][] board = new String[][]{
            {
                    P2, ET, P2, ET, P2
            },
            {
                    ET, ET, K2, ET, P2
            },
            {
                    ET, ET, ET, ET, ET
            },
            {
                    ET, ET, K1, P1, ET
            },
            {
                    P1, P1, ET, ET, ET
            },
        };

        Board chessTamaBoard = getBoard(board);
        chessTamaBoard.setCurrentPlayer(Player.P2);

        List<Card> p2Cards = chessTamaBoard.getP2Cards();
        p2Cards.clear();
        p2Cards.add(Card.BOAR);
        p2Cards.add(Card.TIGER);
        chessTamaBoard.setUpcomingCard(Player.P2, Card.GOOSE);

        List<Card> p1Cards = chessTamaBoard.getP1Cards();
        p1Cards.clear();
        p1Cards.add(Card.EEL);
        p1Cards.add(Card.FROG);
        chessTamaBoard.setUpcomingCard(Player.P1, Card.EMPTY);

        //Board chessTamaBoard = new Board.Builder().build();
        log.info("Board = {}", chessTamaBoard);
        chessTamaBoard.printBoardOnly();
        ScoreMoves scoreMoves = MiniMaxTester.getMiniMaxWithAlphaBetaResult(chessTamaBoard, 1);
        log.info("ScoreMoves = {}", scoreMoves);
    }

    @SuppressWarnings({"PMD.UseVarargs"})
    public static Board getBoard(final String[][] board) {
        if (board.length != Board.MAX_ROWS || board[0].length != Board.MAX_COLS) {
            throw new IllegalArgumentException("Board is not 5 x 5");
        }

        int p1King = 0;
        int p1Pawns = 0;
        int p2King = 0;
        int p2Pawns = 0;

        int p1KingCount = 0;
        int p1PawnsCount = 0;
        int p2KingCount = 0;
        int p2PawnsCount = 0;

        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int col = 0; col < Board.MAX_COLS; col++) {
                String value = board[row][col];
                if (!VALID_TOKENS.contains(value)) {
                    throw new IllegalArgumentException("value = " + value + " not in VALID_TOKENS");
                }
                switch (value) {
                    case "K1":
                        p1King |= 1;
                        p1KingCount++;
                        break;
                    case "P1":
                        p1Pawns |= 1;
                        p1PawnsCount++;
                        break;
                    case "K2":
                        p2King |= 1;
                        p2KingCount++;
                        break;
                    case "P2":
                        p2Pawns |= 1;
                        p2PawnsCount++;
                        break;
                    default:
                        break;
                }
                p1King <<= 1;
                p1Pawns <<= 1;
                p2King <<= 1;
                p2Pawns <<= 1;
            }
        }

        // left shift (31-25) = 6 times
        final int times = Board.BOARD_INDEX_MAX - (Board.MAX_COLS * Board.MAX_ROWS);
        p1King <<= times;
        p1Pawns <<= times;
        p2King <<= times;
        p2Pawns <<= times;

        validate(p1KingCount, p1PawnsCount, p2KingCount, p2PawnsCount);

        return new Board.Builder()
                        .withP1King(p1King)
                        .withP1Pawns(p1Pawns)
                        .withP2King(p2King)
                        .withP2Pawns(p2Pawns)
                        .build();
    }

    private static void validate(final int p1KingCount,
                                 final int p1PawnsCount,
                                 final int p2KingCount,
                                 final int p2PawnsCount) {
        if (p1KingCount != MIN_KINGS) {
            throw new IllegalArgumentException("K1 count has to be " + MIN_KINGS + ", p1KingCount =  " + p1KingCount);
        }

        if (p2KingCount != MIN_KINGS) {
            throw new IllegalArgumentException("K2 count has to be " + MIN_KINGS + ", p1KingCount =  " + p1KingCount);
        }

        if (p1PawnsCount > MAX_PAWNS) {
            throw new IllegalArgumentException("P1 Pawns > " + MAX_PAWNS + ", p1PawnsCount = " + p1PawnsCount);
        }

        if (p2PawnsCount > MAX_PAWNS) {
            throw new IllegalArgumentException("P2 Pawns > " + MAX_PAWNS + ", p2PawnsCount = " + p2PawnsCount);
        }
    }

}
