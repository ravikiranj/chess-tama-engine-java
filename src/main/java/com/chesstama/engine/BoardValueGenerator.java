package com.chesstama.engine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("PMD")
public class BoardValueGenerator {

    public static void main(String[] args) {
        final int[][] board = new int[][]{
            {
                0, 0, 0, 0, 0
            },
            {
                0, 0, 0, 0, 0
            },
            {
                0, 0, 0, 0, 0
            },
            {
                0, 0, 0, 0, 0
            },
        };

        printBoard(board);
        log.info("Value = {}", getBoardValue(board));


    }

    private static void printBoard(final int[][] board) {

    }

    private static int getBoardValue(final int[][] board) {
        return 0;
    }

}
