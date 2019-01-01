package com.chesstama.backend.eval;

import com.chesstama.backend.engine.Card;
import com.chesstama.backend.engine.Position;

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
}
