package com.chesstama.engine;

import com.chesstama.util.BoardUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Card {
    /*
    (top, left)     = (0, 0)
    (top, right)    = (0, 4)
    (bottom, left)  = (4, 0)
    (bottom, right) = (4, 4)

               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .
     */
    MONKEY(0x280A000),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  X  M  X  .
         .  .  .  .  .
         .  .  .  .  .
     */
    ELEPHANT(0x2940000),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  .  M  .  .
         .  .  X  .  .
         .  .  .  .  .
    */
    MANTIS(0x2804000),


    /*
               ↑
         .  .  .  .  .
         X  .  .  .  X
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .
     */
    DRAGON(0x440A000),

    /*
               ↑
         .  .  X  .  .
         .  .  .  .  .
         .  .  M  .  .
         .  .  X  .  .
         .  .  .  .  .
     */
    TIGER(0x20004000),
    EMPTY(0x00000000);

    private final int value;

    private static final int CARD_CENTER_ROW = 2;
    private static final int CARD_CENTER_COL = 2;

    private static final Map<Integer, Card> CARD_MAP = Arrays.stream(Card.values())
        .collect(Collectors.toMap(Card::getValue, Function.identity()));

    Card(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public List<Position> getRelativeMoves(final Player player) {
        List<Position> result = new ArrayList<>();
        int cardPosition = value;

        while (cardPosition != 0) {
            // Extract rightmost set bit
            int pos = BoardUtil.get1DBoardPosition(cardPosition);

            // Add position to result
            Position validMove = BoardUtil.get2DBoardPosition(pos);
            Position tempMove = new Position(validMove.getRow() - CARD_CENTER_ROW, validMove.getCol() - CARD_CENTER_COL);
            Position relativeMove = player == Player.P1 ? tempMove : tempMove.negate();

            result.add(relativeMove);

            // Unset bit at pos
            cardPosition = cardPosition & ~(1 << (Board.BOARD_INDEX_MAX - pos));
        }

        return result;
    }

    public boolean isValidMove(final Player player,
                               final Position from,
                               final Position to) {
        List<Position> relativeMoves = getRelativeMoves(player);

        for (Position relativeMove : relativeMoves) {
            if (from.add(relativeMove).equals(to)) {
                return true;
            }
        }

        return false;
    }

    public List<Position> getAbsoluteMoves() {
        List<Position> relativeMoves = getRelativeMoves(Player.P1);

        return relativeMoves.stream()
                            .map(pos -> new Position(CARD_CENTER_ROW + pos.getRow(), CARD_CENTER_COL + pos.getCol()))
                            .collect(Collectors.toList());
    }

    public static Card getCardFromValue(final int value) {
        return CARD_MAP.getOrDefault(value, EMPTY);
    }

    public void printCard() {
        System.out.println("Card = " + this);
        System.out.println("==============================");
        int maxEntries = Board.MAX_ROWS * Board.MAX_COLS;
        int bitMask = 0x80000000;

        int cardValue = getValue();
        int index = 0;
        while (index < maxEntries) {
            if ((cardValue & bitMask) == bitMask) {
                System.out.print("1 ");
            } else {
                System.out.print("0 ");
            }
            cardValue <<= 1;
            index++;
            if (index % Board.MAX_COLS == 0) {
                System.out.println();
            }
        }
        System.out.println("==============================");
    }

}
