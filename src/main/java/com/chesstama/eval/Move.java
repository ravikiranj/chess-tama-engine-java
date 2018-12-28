package com.chesstama.eval;

import com.chesstama.engine.Card;
import com.chesstama.engine.PieceType;
import com.chesstama.engine.Player;
import com.chesstama.engine.Position;

import java.util.Objects;

public class Move {
    private final Player player;
    private final Card card;
    private final PieceType pieceType;
    private final Position from;
    private final Position to;

    public Move(final Player player,
                final Card card,
                final PieceType pieceType,
                final Position from,
                final Position to) {
        this.player = player;
        this.card = card;
        this.pieceType = pieceType;
        this.from = from;
        this.to = to;
    }

    public Player getPlayer() {
        return player;
    }

    public Card getCard() {
        return card;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move) o;
        return player == move.player &&
                card == move.card &&
                pieceType == move.pieceType &&
                from.equals(move.from) &&
                to.equals(move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, card, pieceType, from, to);
    }

    @Override
    public String toString() {
        return "Move{" +
                "player=" + player +
                ", card=" + card +
                ", pieceType=" + pieceType +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
