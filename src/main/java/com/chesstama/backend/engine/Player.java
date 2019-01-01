package com.chesstama.backend.engine;

public enum Player {
    P1,
    P2;

    public Player getOpponent() {
        return this == P1 ? P2 : P1;
    }
}
