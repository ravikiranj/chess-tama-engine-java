package com.chesstama.testers;

import com.chesstama.engine.Board;
import com.chesstama.eval.MiniMax;
import com.chesstama.eval.Score;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MiniMaxTester {
    public static void main(final String[] args) {
        Board board = new Board.Builder().build();
        int maxDepth = 3;

        Score score = MiniMax.getBestMove(board, maxDepth, true);
        log.info("Score = {}", score);
    }
}
