package com.chesstama.eval;

import static com.chesstama.eval.EvalConstants.BASE_SCORE;

public enum EvalRule {
    OPP_KING_CAPTURE(BASE_SCORE * 1000),
    OPP_KING_HOME(BASE_SCORE * 1000),
    OPP_PAWN_CAPTURE(BASE_SCORE * 100),
    UNIQUE_MOVE(BASE_SCORE * 25);

    private final long score;

    EvalRule(final long score) {
        this.score = score;
    }

    public long getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "EvalRule{" +
                "name=" + name() +
                ", score=" + score +
                '}';
    }
}
