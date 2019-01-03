package com.chesstama.backend.engine;

import com.chesstama.backend.eval.CardMove;
import com.chesstama.backend.eval.Move;
import com.chesstama.backend.eval.PiecePosition;
import com.chesstama.backend.util.BoardUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static final Position P1_KING_SLOT = new Position(4, 2);
    public static final Position P2_KING_SLOT = new Position(0, 2);

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

    private boolean gameOver;
    private Optional<Player> gameWinner;

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

        this.gameOver = builder.gameOver;
        this.gameWinner = builder.gameWinner;

        assertValidCardState();

    }

    public Board copy() {
        Builder builder = new Builder()
            .withP1King(p1King)
            .withP1Pawns(p1Pawns)
            .withP2King(p2King)
            .withP2Pawns(p2Pawns)
            .withP1Cards(copyList(p1Cards))
            .withP1UpcomingCard(p1UpcomingCard)
            .withP2Cards(copyList(p2Cards))
            .withP2UpcomingCard(p2UpcomingCard)
            .withCurrentPlayer(currentPlayer)
            .withGameOver(gameOver)
            .withGameWinner(gameWinner);

        return new Board(builder);
    }

    private <T> List<T> copyList(final List<T> list) {
        if (list == null) {
            return null;
        }

        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        return list.stream()
                   .collect(Collectors.toList());
    }

    public void makeMove(final Move move) {
        if (gameOver) {
            throw new RuntimeException("Game is over and was already won by " + gameWinner.get());
        }

        Position from = move.getFrom();
        Position to = move.getTo();
        Card playedCard = move.getCard();

        Position kingPos = getKingPosition(currentPlayer);
        Set<Position> pawnPositions = getPawnPositions(currentPlayer);
        Set<Position> piecePositions = new ImmutableSet.Builder<Position>()
            .add(kingPos)
            .addAll(pawnPositions)
            .build();

        validateMove(currentPlayer, move, playedCard, piecePositions);

        PieceType pieceTypeToMove = from.equals(kingPos) ? PieceType.KING : PieceType.PAWN;

        Player opponent = currentPlayer.getOpponent();
        Position opponentKingPos = getKingPosition(opponent);
        Set<Position> opponentPawnPositions = getPawnPositions(opponent);
        Position opponentKingHome = currentPlayer == Player.P1 ? P2_KING_SLOT : P1_KING_SLOT;

        Position toCopy = to.copy();
        // King Movement
        if (pieceTypeToMove == PieceType.KING) {
            // Perform the king move
            kingPos = toCopy;

            if (to.equals(opponentKingPos)) {
                // Capture Opponent King
                opponentKingPos = null;
                triggerGameOver();
            } else if (to.equals(opponentKingHome)) {
                // Reach Opponent King Home
                triggerGameOver();
            } else if (opponentPawnPositions.contains(to)) {
                // Capture Pawn
                opponentPawnPositions.remove(to);
            }
        } else {
            // Perform the pawn move
            pawnPositions.remove(from);
            pawnPositions.add(toCopy);

            if (to.equals(opponentKingPos)) {
                // Capture Opponent King
                opponentKingPos = null;
                triggerGameOver();
            } else if (opponentPawnPositions.contains(to)) {
                // Capture Opponent Pawn
                opponentPawnPositions.remove(to);
            }
        }

        updateGameState(
                kingPos,
                pawnPositions,
                opponentKingPos,
                opponentPawnPositions,
                playedCard
        );
    }

    private void updateGameState(final Position kingPos,
                                 final Set<Position> pawnPositions,
                                 final Position opponentKingPos,
                                 final Set<Position> opponentPawnPositions,
                                 final Card playedCard) {
        // Remove played card
        List<Card> cards = getCards(currentPlayer);
        cards.remove(playedCard);

        // Fetch upcoming card and add it to list of playable cards
        cards.add(getUpcomingCard(currentPlayer));

        // Update upcoming card for current player and opponent
        setUpcomingCard(currentPlayer, Card.EMPTY);
        setUpcomingCard(currentPlayer.getOpponent(), playedCard);

        // Update king & pawn positions and currentPlayer
        if (currentPlayer == Player.P1) {
            // kingPos and pawnPositions is used to set P1 King & P1 Pawns and vice-versa
            updateKingAndPawnPositions(kingPos, pawnPositions, opponentKingPos, opponentPawnPositions);
            if (!gameOver) {
                currentPlayer = Player.P2;
            }
        } else {
            updateKingAndPawnPositions(opponentKingPos, opponentPawnPositions, kingPos, pawnPositions);
            if (!gameOver) {
                currentPlayer = Player.P1;
            }
        }

    }

    private void updateKingAndPawnPositions(final Position kingPos,
                                            final Set<Position> pawnPositions,
                                            final Position opponentKingPos,
                                            final Set<Position> opponentPawnPositions) {
        this.p1King = getValueWithBitSetAtPos(0, getSetBitPosFromPosition(kingPos));
        this.p1Pawns = getPawnPositions(pawnPositions);

        this.p2King = getValueWithBitSetAtPos(0, getSetBitPosFromPosition(opponentKingPos));
        this.p2Pawns = getPawnPositions(opponentPawnPositions);
    }

    private int getPawnPositions(final Set<Position> pawnPositions) {
        if (pawnPositions.isEmpty()) {
            return 0;
        }

        return pawnPositions.stream()
                            .map(this::getSetBitPosFromPosition)
                            .map(pos -> getValueWithBitSetAtPos(0, pos))
                            .reduce((a, b) -> a | b)
                            .orElseThrow(() -> new RuntimeException("Unable to compute p1Pawns"));
    }

    private int getSetBitPosFromPosition(final Position position) {
        if (position == null) {
            return -1;
        }

        return position.getRow() * Board.MAX_ROWS + position.getCol();
    }

    private int getValueWithBitSetAtPos(final int value,
                                        final int bitPos) {
        if (bitPos < 0) {
            return 0;
        }

        return value | (1 << (BOARD_INDEX_MAX - bitPos));
    }

    private void triggerGameOver() {
        gameWinner = Optional.of(currentPlayer);
        gameOver = true;
    }

    private void validateMove(final Player currentPlayer,
                              final Move move,
                              final Card card,
                              final Set<Position> piecePositions) {
        Position from = move.getFrom();
        Position to = move.getTo();

        if (!from.isValid() || !to.isValid()) {
            throw new IllegalArgumentException("From/To Position out of bounds, from = " + from + ", to = " + to);
        }

        if (!piecePositions.contains(from)) {
            throw new IllegalArgumentException("From position doesn't contain any of the current player pieces, from = "
                    + from + ", piecePositions = " + piecePositions);
        }

        List<Card> cards = getCards(currentPlayer);
        if (!cards.contains(card)) {
            throw new IllegalArgumentException("Card = " + card + " not in the list of playable cards for current player = " + cards);
        }

        if (!card.isValidMove(currentPlayer, from, to)) {
            throw new IllegalArgumentException("Cannot play move = " + move + " with card = " + card);
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Optional<Player> getGameWinner() {
        return gameWinner;
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
        if (kingPosition == 0) {
            return null;
        }
        int oneDimensionBoardPos = BoardUtil.get1DBoardPosition(kingPosition);
        return BoardUtil.get2DBoardPosition(oneDimensionBoardPos);
    }

    public Set<Position> getPawnPositions(final Player player) {
        Set<Position> result = new HashSet<>(MAX_PAWNS);
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

    public Set<PiecePosition> getAllPiecePositions(final Player player) {
        Set<PiecePosition> piecePositions = new HashSet<>();
        piecePositions.add(new PiecePosition(PieceType.KING, getKingPosition(player)));

        getPawnPositions(player).stream()
                                .forEach(position -> piecePositions.add(new PiecePosition(PieceType.PAWN, position)));

        return Collections.unmodifiableSet(piecePositions);

    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public Set<CardMove> getAllCardMoves(final Player player) {
        Set<CardMove> cardMoves = new HashSet<>();

        for (Card card : getCards(player)) {
            card.getRelativeMoves(player)
                .stream()
                .forEach(move -> cardMoves.add(new CardMove(card, move)));
        }

        return Collections.unmodifiableSet(cardMoves);
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

    public void setUpcomingCard(final Player player, final Card card) {
        if (player == Player.P1) {
            p1UpcomingCard = card;
        } else {
            p2UpcomingCard = card;
        }
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
        Set<Position> p1Pawns = getPawnPositions(Player.P1);

        Position p2King = getKingPosition(Player.P2);
        Set<Position> p2Pawns = getPawnPositions(Player.P2);

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

    public void setP1King(final int p1King) {
        this.p1King = p1King;
    }

    public void setP1Pawns(final int p1Pawns) {
        this.p1Pawns = p1Pawns;
    }

    public void setP2King(final int p2King) {
        this.p2King = p2King;
    }

    public void setP2Pawns(final int p2Pawns) {
        this.p2Pawns = p2Pawns;
    }

    public void setP1UpcomingCard(final Card p1UpcomingCard) {
        this.p1UpcomingCard = p1UpcomingCard;
    }

    public void setP2UpcomingCard(final Card p2UpcomingCard) {
        this.p2UpcomingCard = p2UpcomingCard;
    }

    public void setCurrentPlayer(final Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setGameOver(final boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setGameWinner(final Optional<Player> gameWinner) {
        this.gameWinner = gameWinner;
    }

    public int getP1King() {
        return p1King;
    }

    public int getP1Pawns() {
        return p1Pawns;
    }

    public int getP2King() {
        return p2King;
    }

    public int getP2Pawns() {
        return p2Pawns;
    }

    public List<Card> getP1Cards() {
        return p1Cards;
    }

    public Card getP1UpcomingCard() {
        return p1UpcomingCard;
    }

    public List<Card> getP2Cards() {
        return p2Cards;
    }

    public Card getP2UpcomingCard() {
        return p2UpcomingCard;
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
        private List<Card> p1Cards;
        private Card p1UpcomingCard = Card.TIGER;

        private List<Card> p2Cards;
        private Card p2UpcomingCard = Card.EMPTY;

        private Player currentPlayer = Player.P1;

        private boolean gameOver;
        private Optional<Player> gameWinner = Optional.empty();

        public Builder() {
            p1Cards = new ArrayList<>();
            p1Cards.add(Card.MONKEY);
            p1Cards.add(Card.ELEPHANT);

            p2Cards = new ArrayList<>();
            p2Cards.add(Card.DRAGON);
            p2Cards.add(Card.MANTIS);
        }

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

        public Builder withGameOver(final boolean gameOver) {
            this.gameOver = gameOver;
            return this;
        }

        public Builder withGameWinner(final Optional<Player> gameWinner) {
            this.gameWinner = gameWinner;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
