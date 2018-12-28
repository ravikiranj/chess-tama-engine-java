package com.chesstama.eval;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Score implements Comparable<Score> {
    public static final Score MAX_SCORE;
    public static final Score MIN_SCORE;

    static {
        MAX_SCORE = new Score();
        MAX_SCORE.add(EvalRule.GAME_WON);

        MIN_SCORE = new Score();
        MIN_SCORE.add(EvalRule.GAME_LOST);
    }

    private static final ScoreMapComparator SCORE_MAP_COMPARATOR = new ScoreMapComparator();

    private final Map<EvalRule, Integer> scoreMap;
    private long totalScore;

    public Score() {
        this(0L);
    }

    public Score(final long totalScore) {
        this.scoreMap = new HashMap<>();
        this.totalScore = totalScore;
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

    @Override
    public int compareTo(final Score o) {
        return Long.compare(this.totalScore, o.totalScore);
    }

    public static Score max(final Score s1, final Score s2) {
        return s1.compareTo(s2) >= 0 ? s1 : s2;
    }

    public static Score min(final Score s1, final Score s2) {
        return s1.compareTo(s2) >= 0 ? s2 : s1;
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
