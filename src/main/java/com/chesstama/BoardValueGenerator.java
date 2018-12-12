package com.chesstama;

import com.chesstama.engine.Board;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardValueGenerator {

    public static void main(final String[] args) {
        final int[][] board = new int[][]{
            {
                0, 0, 1, 0, 0
            },
            {
                0, 0, 0, 0, 0
            },
            {
                0, 0, 0, 0, 0
            },
            {
                0, 0, 1, 0, 0
            },
            {
                0, 0, 0, 0, 0
            }
        };

        printBoard(board);
        final int value = getBoardValue(board);
        final String hexValue = String.format("%02X", value);
        log.info("Value in Dec = {}, Hex = 0x{}", value, hexValue);
    }

    @SuppressWarnings({"PMD.UseVarargs", "PMD.SystemPrintln"})
    private static void printBoard(final int[][] board) {
        System.out.println("Board");
        System.out.println("==============================");
        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int col = 0; col < Board.MAX_COLS; col++) {
                if (col == Board.MAX_COLS - 1) {
                    System.out.print(board[row][col]);
                } else {
                    System.out.print(board[row][col] + " ");
                }
            }
            System.out.println();
        }
        System.out.println("==============================");
    }

    @SuppressWarnings("PMD.UseVarargs")
    private static int getBoardValue(final int[][] board) {
        int value = 0;
        boolean first = true;

        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int col = 0; col < Board.MAX_COLS; col++) {
                if (first) {
                    value = board[row][col];
                    first = false;
                } else {
                    value <<= 1;
                    value |= board[row][col];
                }
            }
        }

        // left shift (32-25) = 7 times
        final int times = Board.BOARD_INDEX_MAX - (Board.MAX_COLS * Board.MAX_ROWS) + 1;
        value <<= times;

        return value;
    }

}
