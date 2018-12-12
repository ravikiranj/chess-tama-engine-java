package com.chesstama;

import com.chesstama.engine.Board;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class BoardGenerator {
    private static final String EMPT = ".";
    private static final Set<String> VALID_TOKENS = ImmutableSet.of("K1", "P1", "K2", "P2", EMPT);

    public static void main(final String[] args) {
        final String[][] board = new String[][]{
            {
                "P2", "P2", "K2", "P2", "P2"
            },
            {
                EMPT, EMPT, EMPT, EMPT, EMPT
            },
            {
                EMPT, EMPT, EMPT, EMPT, EMPT
            },
            {
                EMPT, EMPT, EMPT, EMPT, EMPT
            },
            {
                "P1", "P1", "K1", "P1", "P1"
            },
        };

        printBoard(board);

        Board chessTamaBoard = getBoard(board);
        log.info("Board = {}", chessTamaBoard);
        chessTamaBoard.printBoardState();
    }

    @SuppressWarnings({"PMD.UseVarargs"})
    private static Board getBoard(final String[][] board) {
        int p1King = 0;
        int p1Pawns = 0;
        int p2King = 0;
        int p2Pawns = 0;

        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int col = 0; col < Board.MAX_COLS; col++) {
                String value = board[row][col];
                if (!VALID_TOKENS.contains(value)) {
                    throw new IllegalArgumentException("value = " + value + " not in VALID_TOKENS");
                }
                switch (value) {
                    case "K1":
                        p1King |= 1;
                        break;
                    case "P1":
                        p1Pawns |= 1;
                        break;
                    case "K2":
                        p2King |= 1;
                        break;
                    case "P2":
                        p2Pawns |= 1;
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

        return new Board(p1King, p1Pawns, p2King, p2Pawns);
    }

    @SuppressWarnings({"PMD.UseVarargs", "PMD.SystemPrintln"})
    private static void printBoard(final String[][] board) {
        System.out.println("Board");
        System.out.println("==============================");
        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int col = 0; col < Board.MAX_COLS; col++) {
                String value = board[row][col];
                if (EMPT.equals(value)) {
                    System.out.print(".." + " ");
                } else {
                    System.out.print(value + " ");
                }
            }
            System.out.println();
        }
        System.out.println("==============================");
    }


}
