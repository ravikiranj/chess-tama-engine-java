package com.chesstama.eval;

import static com.chesstama.eval.Evaluator.BASE_SCORE;

public enum EvalRule {
    OPP_KING_CAPTURE(BASE_SCORE * 100_000),
    OPP_KING_HOME(BASE_SCORE * 100_000),
    OPP_PAWN_CAPTURE(BASE_SCORE * 5000),
    UNIQUE_MOVE(BASE_SCORE * 250),
    KING_CAPTURE(BASE_SCORE * -50_000),
    KING_HOME(BASE_SCORE * -50_000),
    PAWN_CAPTURE(BASE_SCORE * -5000),
    GAME_WON(BASE_SCORE * Long.MAX_VALUE),
    GAME_LOST(BASE_SCORE * Long.MIN_VALUE);

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
