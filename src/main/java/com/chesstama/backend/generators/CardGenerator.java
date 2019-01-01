package com.chesstama.backend.generators;

import com.chesstama.backend.engine.Board;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardGenerator {

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

        printCard(board);
        final int value = getCardValue(board);
        final String hexValue = String.format("%08X", value);
        log.info("Value in Dec = {}, Hex = 0x{}", value, hexValue);
    }

    private static void printCard(final int[][] board) {
        System.out.println("Card");
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

    public static int getCardValue(final int[][] board) {
        int value = 0;

        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int col = 0; col < Board.MAX_COLS; col++) {
                value |= board[row][col];
                value <<= 1;
            }
        }

        // left shift (31-25) = 6 times
        final int times = Board.BOARD_INDEX_MAX - (Board.MAX_COLS * Board.MAX_ROWS);
        value <<= times;

        return value;
    }

}
