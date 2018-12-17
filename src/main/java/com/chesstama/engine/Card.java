package com.chesstama.engine;

import java.util.Arrays;
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

    private static final Map<Integer, Card> CARD_MAP = Arrays.stream(Card.values())
        .collect(Collectors.toMap(Card::getValue, Function.identity()));

    Card(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
