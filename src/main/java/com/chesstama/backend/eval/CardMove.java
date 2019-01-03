package com.chesstama.backend.eval;

import com.chesstama.backend.engine.Card;
import com.chesstama.backend.engine.Position;

import java.util.Objects;

public class CardMove {
    private final Card card;
    private final Position relativeMove;

    public CardMove(final Card card,
                    final Position relativeMove) {
        this.card = card;
        this.relativeMove = relativeMove;
    }

    public Card getCard() {
        return card;
    }

    public Position getRelativeMove() {
        return relativeMove;
    }

    @Override
    public String toString() {
        return "CardMove{" +
                "card=" + card +
                ", relativeMove=" + relativeMove +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardMove cardMove = (CardMove) o;
        return card == cardMove.card &&
            Objects.equals(relativeMove, cardMove.relativeMove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, relativeMove);
    }
}
