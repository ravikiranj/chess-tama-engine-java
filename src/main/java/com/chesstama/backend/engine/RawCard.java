package com.chesstama.backend.engine;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.chesstama.backend.engine.RawCard.CardColor.BLUE;
import static com.chesstama.backend.engine.RawCard.CardColor.RED;

@Slf4j
public enum RawCard {
    /*
    (top, left)     = (1, 1)
    (top, right)    = (1, 5)
    (bottom, left)  = (5, 1)
    (bottom, right) = (5, 5)

               ↑
         .  .  .  .  .
         X  .  .  .  X
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .

     */
    DRAGON(RED, ImmutableSet.of(
        new Position(-1, -2),
        new Position(-1, 2),
        new Position(1, -1),
        new Position(1, 1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         .  X  M  X  .
         .  .  .  .  .
         .  .  .  .  .
     */
    BOAR(RED, ImmutableSet.of(
        new Position(0, -1),
        new Position(0, 1),
        new Position(-1, 0)
    )),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  .  M  .  .
         .  .  X  .  .
         .  .  .  .  .

     */
    MANTIS(RED, ImmutableSet.of(
        new Position(1, 0),
        new Position(-1, -1),
        new Position(-1, 1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  X  M  X  .
         .  .  .  .  .
         .  .  .  .  .
     */
    ELEPHANT(RED, ImmutableSet.of(
        new Position(0, -1),
        new Position(0, 1),
        new Position(-1, -1),
        new Position(-1, 1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  .  .  X  .
         .  X  M  X  .
         .  X  .  .  .
         .  .  .  .  .
     */
    ROOSTER(RED, ImmutableSet.of(
        new Position(0, -1),
        new Position(0, 1),
        new Position(-1, 1),
        new Position(1, -1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  .  .  X  .
         .  X  M  .  .
         .  .  .  X  .
         .  .  .  .  .
     */
    COBRA(RED, ImmutableSet.of(
        new Position(1, 1),
        new Position(-1, 1),
        new Position(0, -1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         .  .  M  X  .
         .  .  X  .  .
         .  .  .  .  .
     */
    OX(RED, ImmutableSet.of(
        new Position(1, 0),
        new Position(-1, 0),
        new Position(0, 1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  .  .  X  .
         .  .  M  .  X
         .  X  .  .  .
         .  .  .  .  .
     */
    RABBIT(RED, ImmutableSet.of(
        new Position(1, -1),
        new Position(0, 2),
        new Position(-1, 1)
    )),

    /*
               ↑
         .  .  X  .  .
         .  .  .  .  .
         .  .  M  .  .
         .  .  X  .  .
         .  .  .  .  .
     */
    TIGER(BLUE, ImmutableSet.of(
        new Position(-2, 0),
        new Position(1, 0)
    )),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         X  .  M  .  X
         .  .  .  .  .
         .  .  .  .  .
     */
    CRAB(BLUE, ImmutableSet.of(
        new Position(-1, 0),
        new Position(0, -2),
        new Position(0, 2)
    )),

    /*
               ↑
         .  .  .  .  .
         .  X  .  .  .
         .  X  M  X  .
         .  .  .  X  .
         .  .  .  .  .
     */
    GOOSE(BLUE, ImmutableSet.of(
        new Position(0, -1),
        new Position(0, 1),
        new Position(1, 1),
        new Position(-1, -1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .
     */
    CRANE(BLUE, ImmutableSet.of(
        new Position(-1, 0),
        new Position(1, -1),
        new Position(1, 1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  .  X  .  .
         .  X  M  .  .
         .  .  X  .  .
         .  .  .  .  .
     */
    HORSE(BLUE, ImmutableSet.of(
        new Position(1, 0),
        new Position(0, -1),
        new Position(-1, 0)
    )),

    /*
               ↑
         .  .  .  .  .
         .  X  .  .  .
         .  .  M  X  .
         .  X  .  .  .
         .  .  .  .  .
     */
    EEL(BLUE, ImmutableSet.of(
        new Position(1, -1),
        new Position(0, 1),
        new Position(-1, -1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  X  .  .  .
         X  .  M  .  .
         .  .  .  X  .
         .  .  .  .  .
     */
    FROG(BLUE, ImmutableSet.of(
        new Position(-1, -1),
        new Position(0, -2),
        new Position(1, 1)
    )),

    /*
               ↑
         .  .  .  .  .
         .  X  .  X  .
         .  .  M  .  .
         .  X  .  X  .
         .  .  .  .  .
     */
    MONKEY(BLUE, ImmutableSet.of(
        new Position(1, -1),
        new Position(1, 1),
        new Position(-1, -1),
        new Position(-1, 1)
    ));

    private final CardColor cardColor;
    private final ImmutableSet<Position> positions;

    RawCard(final CardColor cardColor, final ImmutableSet<Position> positions) {
        this.cardColor = cardColor;
        this.positions = positions;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public enum CardColor {
        RED,
        BLUE
    }
}
