package com.chesstama.eval;

import java.util.HashMap;
import java.util.Map;

public class Score {
    private final Map<EvalRule, Integer> scoreMap;
    private long totalScore;

    public Score() {
        this.scoreMap = new HashMap<>();
    }

    public void add(final EvalRule evalRule) {
        add(evalRule, 1);
    }

    public void add(final EvalRule evalRule, final int count) {
        // putIfAbsent returns non-null if key is present (and a NO-OP)
        if (scoreMap.putIfAbsent(evalRule, count) != null) {
            scoreMap.computeIfPresent(evalRule, (k, v) -> v + count);
        }
        totalScore += evalRule.getScore() * count;
    }

    public Map<EvalRule, Integer> getScoreMap() {
        return scoreMap;
    }

    public long getTotalScore() {
        return totalScore;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<EvalRule, Integer> entry : scoreMap.entrySet()) {
            EvalRule evalRule = entry.getKey();
            Integer count = entry.getValue();
            stringBuilder.append(
                String.format(
                    "\nRule = %s, Count = %d, Score = %d\n",
                    evalRule,
                    count,
                    evalRule.getScore() * count
                )
            );
        }
        stringBuilder.append(String.format("Total Score = %d\n", totalScore));

        return stringBuilder.toString();
    }
}
