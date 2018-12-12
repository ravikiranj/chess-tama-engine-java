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
    MONKEY(0),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  X  M  X  .
         .  .  .  .  .
         .  .  .  .  .
     */
    ELEPHANT(0),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  .  M  .  .
         .  .  X  .  .
         .  .  .  .  .
    */
    MANTIS(0),


    /*
               ↑
         .  .  .  .  .
         X  .  .  .  X
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .
     */
    DRAGON(0),

    /*
               ↑
         .  .  X  .  .
         .  .  .  .  .
         .  .  M  .  .
         .  .  X  .  .
         .  .  .  .  .
     */
    TIGER(0),
    UNKNOWN(Integer.MAX_VALUE);

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
        return CARD_MAP.getOrDefault(value, UNKNOWN);
    }

}
