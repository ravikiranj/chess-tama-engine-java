package com.chesstama.backend.generators;

import com.chesstama.backend.engine.Board;
import com.chesstama.backend.engine.Card;
import com.chesstama.backend.engine.Position;
import com.chesstama.backend.engine.RawCard;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CardGenerator {

    public static void main(final String[] args) {
        final int[][] board = new int[][]{
            {
                0, 0, 0, 0, 0
            },
            {
                0, 0, 1, 0, 0
            },
            {
                0, 1, 0, 1, 0           // CENTER ROW
            },
            {
                0, 0, 0, 0, 0
            },
            {
                0, 0, 0, 0, 0
            }
        };

        printCard(board);
        int value = getCardValue(board);
        String hexValue = String.format("%08X", value);
        log.info("Value in Dec = {}, Hex = 0x{}", value, hexValue);

        printAllRawCardValues();
    }

    private static void printAllRawCardValues() {
        for (RawCard card : RawCard.values()) {
            int value = getCardValue(getRawCardBoard(card));
            String hexValue = String.format("%08X", value);
            log.info("Card = {}, Hex = 0x{}", card.name(), hexValue);
        }
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private static int[][] getRawCardBoard(final RawCard card) {
        int[][] board = new int[Board.MAX_ROWS][Board.MAX_COLS];

        Set<Position> cardPositions = card.getPositions()
            .stream()
            .map(pos -> new Position(Card.CARD_CENTER_ROW + pos.getRow(), Card.CARD_CENTER_COL + pos.getCol()))
            .collect(Collectors.toSet());

        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int col = 0; col < Board.MAX_COLS; col++) {
                Position pos = new Position(row, col);
                if (cardPositions.contains(pos)) {
                    board[row][col] = 1;
                }
            }
        }

        return board;
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
