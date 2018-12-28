package com.chesstama.eval;

import com.chesstama.engine.Card;
import com.chesstama.engine.Position;

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
