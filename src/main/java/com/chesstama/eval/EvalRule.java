package com.chesstama.eval;

import static com.chesstama.eval.EvalConstants.BASE_SCORE;

public enum EvalRule {
    OPP_KING_CAPTURE(BASE_SCORE * 10000),
    OPP_KING_HOME(BASE_SCORE * 10000),
    OPP_PAWN_CAPTURE(BASE_SCORE * 1000),
    UNIQUE_MOVE(BASE_SCORE * 250),
    KING_CAPTURE(BASE_SCORE * -5000),
    KING_HOME(BASE_SCORE * -5000),
    PAWN_CAPTURE(BASE_SCORE * -200);

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
