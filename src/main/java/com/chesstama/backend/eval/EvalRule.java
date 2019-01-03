package com.chesstama.backend.eval;

import static com.chesstama.backend.eval.Evaluator.BASE_SCORE;

public enum EvalRule {
    OPP_KING_CAPTURE(BASE_SCORE * 100_000),
    OPP_KING_HOME(BASE_SCORE * 100_000),
    OPP_PAWN_CAPTURE(BASE_SCORE * 20_000),
    UNIQUE_MOVE(BASE_SCORE * 50),
    KING_CAPTURE(BASE_SCORE * -150_000),
    KING_HOME(BASE_SCORE * -150_000),
    PAWN_CAPTURE(BASE_SCORE * -25_000),
    GAME_WON(BASE_SCORE * 500_000),
    GAME_LOST(BASE_SCORE * -500_000),
    MAX_SCORE(BASE_SCORE * Long.MAX_VALUE),
    MIN_SCORE(BASE_SCORE * Long.MIN_VALUE);

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
