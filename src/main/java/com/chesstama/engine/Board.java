package com.chesstama.engine;

import com.chesstama.util.BoardUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Board {
    /*
(0-7) | (8-15) | (16-23) | (24-31)
                 P2
Col     0    1    2    3    4
Row  +----+----+----+----+----+
 0   |  0 |  1 |  2 |  3 |  4 |
     +----+----+----+----+----+
 1   |  5 |  6 |  7 |  8 |  9 |
     +----+----+----+----+----+
 2   | 10 | 11 | 12 | 13 | 14 |
     +----+----+----+----+----+
 3   | 15 | 16 | 17 | 18 | 19 |
     +----+----+----+----+----+
 4   | 20 | 21 | 22 | 23 | 24 |
     +----+----+----+----+----+
                 P1
    */

    private static final int MAX_PAWNS = 4;
    private static final int MAX_CARDS = 2;

    public static final int BOARD_INDEX_MAX = 31;
    public static final int MAX_ROWS = 5;
    public static final int MAX_COLS = 5;

    public static final int MIN_ROW_INDEX = 0;
    public static final int MAX_ROW_INDEX = 4;

    public static final int MIN_COL_INDEX = 0;
    public static final int MAX_COL_INDEX = 4;

    // 00 | 00 | 02 | 00
    private int p1King;

    // 00 | 00 | 0D | 80
    private int p1Pawns;

    // 20 | 00 | 00 | 00
    private int p2King;

    // D8 | 00 | 00 | 00
    private int p2Pawns;

    // Cards
    private final List<Card> p1Cards;
    private Card p1UpcomingCard;

    private final List<Card> p2Cards;
    private Card p2UpcomingCard;

    private Player currentPlayer;

    private Board(final Builder builder) {
        this.p1King = builder.p1King;
        this.p1Pawns = builder.p1Pawns;

        this.p2King = builder.p2King;
        this.p2Pawns = builder.p2Pawns;

        this.p1Cards = builder.p1Cards;
        this.p1UpcomingCard = builder.p1UpcomingCard;

        this.p2Cards = builder.p2Cards;
        this.p2UpcomingCard = builder.p2UpcomingCard;

        this.currentPlayer = builder.currentPlayer;

        assertValidCardState();

    }

    private void assertValidCardState() {
        Preconditions.checkNotNull(p1Cards);
        Preconditions.checkArgument(p1Cards.size() == MAX_CARDS);
        Preconditions.checkNotNull(p1UpcomingCard);

        Preconditions.checkNotNull(p2Cards);
        Preconditions.checkArgument(p2Cards.size() == MAX_CARDS);
        Preconditions.checkNotNull(p2UpcomingCard);
    }

    public Position getKingPosition(final Player player) {
        int kingPosition = player == Player.P1 ? p1King : p2King;
        int oneDimensionBoardPos = BoardUtil.get1DBoardPosition(kingPosition);
        return BoardUtil.get2DBoardPosition(oneDimensionBoardPos);
    }

    public List<Position> getPawnPositions(final Player player) {
        List<Position> result = new ArrayList<>(MAX_PAWNS);
        int pawnPosition = player == Player.P1 ? p1Pawns : p2Pawns;
        int pawnsFound = 0;

        while (pawnPosition != 0 && pawnsFound < MAX_PAWNS) {
            // Extract rightmost set bit
            int pos = BoardUtil.get1DBoardPosition(pawnPosition);

            // Add position to result
            result.add(BoardUtil.get2DBoardPosition(pos));

            // Unset bit at pos
            pawnPosition = pawnPosition & ~(1 << (BOARD_INDEX_MAX - pos));

            pawnsFound++;
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Board {");
        result.append(String.format(" p1King = %d (0x%08X)", p1King, p1King));
        result.append(String.format(", p1Pawns = %d (0x%08X)", p1Pawns, p1Pawns));
        result.append(String.format(", p2King = %d (0x%08X)", p2King, p2King));
        result.append(String.format(", p2Pawns = %d (0x%08X)", p2Pawns, p2Pawns));
        result.append(" }");

        return result.toString();
    }

    public List<Card> getCards(final Player player) {
        return player == Player.P1 ? p1Cards : p2Cards;
    }

    public Card getUpcomingCard(final Player player) {
        return player == Player.P1 ? p1UpcomingCard: p2UpcomingCard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void printBoardState() {
        System.out.println("Current Player = " + currentPlayer);
        for (Player player : Player.values()) {
            System.out.println("Player = " + player);
            System.out.println("==============================");
            log.info("King Position = {}", this.getKingPosition(player));
            log.info("Pawn Positions = {}", this.getPawnPositions(player));
            log.info("Main Cards");
            System.out.println("==============================");
            for (Card card : this.getCards(player)) {
                card.printCard();
            }
            log.info("Upcoming Card");
            this.getUpcomingCard(player).printCard();
            System.out.println("==============================");
        }
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void printBoardOnly() {
        Position p1King = getKingPosition(Player.P1);
        List<Position> p1Pawns = getPawnPositions(Player.P1);

        Position p2King = getKingPosition(Player.P2);
        List<Position> p2Pawns = getPawnPositions(Player.P2);

        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int col = 0; col < Board.MAX_COLS; col++) {
                Position p = new Position(row, col);
                if (p.equals(p1King)) {
                    System.out.print("K1  ");
                } else if (p.equals(p2King)) {
                    System.out.print("K2  ");
                } else if (p1Pawns.contains(p)) {
                    System.out.print("P1  ");
                } else if (p2Pawns.contains(p)) {
                    System.out.print("P2  ");
                } else {
                    System.out.print("..  ");
                }
            }
            System.out.println();
        }

    }

    public static final class Builder {
        // 00 | 00 | 02 | 00
        private int p1King = 0x00000200;

        // 00 | 00 | 0D | 80
        private int p1Pawns = 0x00000D80;

        // 20 | 00 | 00 | 00
        private int p2King = 0x20000000;

        // D8 | 00 | 00 | 00
        private int p2Pawns = 0xD8000000;

        // Default Cards
        private List<Card> p1Cards = ImmutableList.of(Card.MONKEY, Card.ELEPHANT);
        private Card p1UpcomingCard = Card.TIGER;

        private List<Card> p2Cards = ImmutableList.of(Card.DRAGON, Card.MANTIS);
        private Card p2UpcomingCard = Card.EMPTY;

        private Player currentPlayer = Player.P1;

        public Builder withP1King(final int p1King) {
            this.p1King = p1King;
            return this;
        }

        public Builder withP1Pawns(final int p1Pawns) {
            this.p1Pawns = p1Pawns;
            return this;
        }

        public Builder withP2King(final int p2King) {
            this.p2King = p2King;
            return this;
        }

        public Builder withP2Pawns(final int p2Pawns) {
            this.p2Pawns = p2Pawns;
            return this;
        }

        public Builder withP1Cards(final List<Card> p1Cards) {
            this.p1Cards = p1Cards;
            return this;
        }

        public Builder withP1UpcomingCard(final Card p1UpcomingCard) {
            this.p1UpcomingCard = p1UpcomingCard;
            return this;
        }

        public Builder withP2Cards(final List<Card> p2Cards) {
            this.p2Cards = p2Cards;
            return this;
        }

        public Builder withP2UpcomingCard(final Card p2UpcomingCard) {
            this.p2UpcomingCard = p2UpcomingCard;
            return this;
        }

        public Builder withCurrentPlayer(final Player currentPlayer) {
            this.currentPlayer = currentPlayer;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
