package com.chesstama.backend.engine;

import com.chesstama.backend.util.BoardUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
         X  .  .  .  X
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .

     */
    DRAGON(0x0440A000),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         .  X  M  X  .
         .  .  .  .  .
         .  .  .  .  .
     */
    BOAR(0x01140000),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  .  M  .  .
         .  .  X  .  .
         .  .  .  .  .

     */
    MANTIS(0x02804000),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  X  M  X  .
         .  .  .  .  .
         .  .  .  .  .
     */
    ELEPHANT(0x02940000),

    /*
               ↑
         .  .  .  .  .
         .  .  .  X  .
         .  X  M  X  .
         .  X  .  .  .
         .  .  .  .  .
     */
    ROOSTER(0x00948000),

    /*
               ↑
         .  .  .  .  .
         .  .  .  X  .
         .  X  M  .  .
         .  .  .  X  .
         .  .  .  .  .
     */
    COBRA(0x00902000),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         .  .  M  X  .
         .  .  X  .  .
         .  .  .  .  .
     */
    OX(0x01044000),

    /*
               ↑
         .  .  .  .  .
         .  .  .  X  .
         .  .  M  .  X
         .  X  .  .  .
         .  .  .  .  .
     */
    RABBIT(0x00828000),

    /*
               ↑
         .  .  X  .  .
         .  .  .  .  .
         .  .  M  .  .
         .  .  X  .  .
         .  .  .  .  .
     */
    TIGER(0x20004000),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         X  .  M  .  X
         .  .  .  .  .
         .  .  .  .  .
     */
    CRAB(0x01220000),

    /*
               ↑
         .  .  .  .  .
         .  X  .  .  .
         .  X  M  X  .
         .  .  .  X  .
         .  .  .  .  .
     */
    GOOSE(0x02142000),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .
     */
    CRANE(0x0100A000),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         .  X  M  .  .
         .  .  X  .  .
         .  .  .  .  .
     */
    HORSE(0x01104000),

    /*
               ↑
         .  .  .  .  .
         .  X  .  .  .
         .  .  M  X  .
         .  X  .  .  .
         .  .  .  .  .
     */
    EEL(0x02048000),

    /*
               ↑
         .  .  .  .  .
         .  X  .  .  .
         X  .  M  .  .
         .  .  .  X  .
         .  .  .  .  .
     */
    FROG(0x02202000),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .
     */
    MONKEY(0x0280A000),
    EMPTY(0x00000000);

    public static final int CARD_CENTER_ROW = 2;
    public static final int CARD_CENTER_COL = 2;

    private static final Map<Integer, Card> CARD_MAP = Arrays.stream(Card.values())
        .collect(Collectors.toMap(Card::getValue, Function.identity()));

    private final int value;

    Card(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public Set<Position> getRelativeMoves(final Player player) {
        Set<Position> result = new HashSet<>();
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
        Set<Position> relativeMoves = getRelativeMoves(player);

        for (Position relativeMove : relativeMoves) {
            if (from.add(relativeMove).equals(to)) {
                return true;
            }
        }

        return false;
    }

    public Set<Position> getAbsoluteMoves() {
       Set<Position> relativeMoves = getRelativeMoves(Player.P1);

        return relativeMoves.stream()
                            .map(pos -> new Position(CARD_CENTER_ROW + pos.getRow(), CARD_CENTER_COL + pos.getCol()))
                            .collect(Collectors.toSet());
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
