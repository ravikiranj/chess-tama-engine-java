package com.chesstama.backend.eval;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.List;
import java.util.Objects;

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
        return ComparisonChain.start()
                              .compare(this.score, o.getScore())
                              // Prefer shorter move set if score is the same
                              .compare(this.moves.size(), o.moves.size(), Ordering.natural().reverse())
                              .result();
    }

    public static ScoreMoves max(final ScoreMoves o1, final ScoreMoves o2) {
        return o1.compareTo(o2) >= 0 ? o1 : o2;
    }

    public static ScoreMoves min(final ScoreMoves o1, final ScoreMoves o2) {
        return o1.compareTo(o2) > 0 ? o2 : o1;
    }

    public boolean isGreaterThan(final ScoreMoves o) {
        return this.compareTo(o) > 0;
    }

    public boolean isGreaterThanOrEqualTo(final ScoreMoves o) {
        return this.compareTo(o) >= 0;
    }

    public boolean isLesserThan(final ScoreMoves o) {
        return this.compareTo(o) < 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoreMoves that = (ScoreMoves) o;
        return score.getTotalScore() == that.score.getTotalScore();
    }

    @Override
    public int hashCode() {
        return Objects.hash(score);
    }
}
