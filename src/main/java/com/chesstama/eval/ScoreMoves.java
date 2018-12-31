package com.chesstama.eval;

import java.util.List;

public class ScoreMoves implements Comparable<ScoreMoves> {
    private final Score score;
    private final List<Move> moves;

    public ScoreMoves(final Score score, final List<Move> moves) {
        this.score = score;
        this.moves = moves;
    }

    public Score getScore() {
        return score;
    }

    public List<Move> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return "ScoreMoves{" +
                "score=" + score +
                ", moves=" + moves +
                '}';
    }

    @Override
    public int compareTo(final ScoreMoves o) {
        return this.score.compareTo(o.getScore());
    }
}
