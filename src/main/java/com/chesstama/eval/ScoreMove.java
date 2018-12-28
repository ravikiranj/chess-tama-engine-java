package com.chesstama.eval;

public class ScoreMove {
    private final Score score;
    private final Move move;

    public ScoreMove(final Score score, final Move move) {
        this.score = score;
        this.move = move;
    }

    public Score getScore() {
        return score;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public String toString() {
        return "ScoreMove{" +
                "score=" + score +
                ", move=" + move +
                '}';
    }
}
