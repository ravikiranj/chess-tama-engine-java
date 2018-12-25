package com.chesstama.eval;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Score {
    private static final ScoreMapComparator SCORE_MAP_COMPARATOR = new ScoreMapComparator();

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
        StringBuilder stringBuilder = new StringBuilder("\n");

        scoreMap.entrySet()
                .stream()
                .sorted(SCORE_MAP_COMPARATOR.reversed())
                .forEach(entry -> {
                    EvalRule evalRule = entry.getKey();
                    Integer count = entry.getValue();
                    stringBuilder.append(
                        String.format(
                            "Rule = %s, Count = %d, Score = %d\n",
                            evalRule,
                            count,
                            evalRule.getScore() * count
                        )
                    );
                });

        stringBuilder.append(String.format("Total Score = %d\n", totalScore));

        return stringBuilder.toString();
    }

    private static class ScoreMapComparator implements Comparator<Map.Entry<EvalRule, Integer>> {

        @Override
        public int compare(final Map.Entry<EvalRule, Integer> o1, final Map.Entry<EvalRule, Integer> o2) {
            long result = o1.getKey().getScore() * o1.getValue() - o2.getKey().getScore() * o2.getValue();
            if (result == 0) {
                return 0;
            }

            return result > 0L ? 1 : -1;
        }
    }
}
