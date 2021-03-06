package com.chesstama.backend.testers;

import com.chesstama.backend.engine.Board;
import com.chesstama.backend.eval.MiniMax;
import com.chesstama.backend.eval.MiniMaxWithAlphaBeta;
import com.chesstama.backend.eval.Score;
import com.chesstama.backend.eval.ScoreMoves;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class MiniMaxTester {
    public static void main(final String[] args) {
        int maxDepth = 5;
        getMiniMaxWithAlphaBetaResult(maxDepth);
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static ScoreMoves getMiniMaxResult(final int maxDepth) {
        long startTime = System.currentTimeMillis();

        Board board = new Board.Builder().build();

        ScoreMoves scoreMoves = MiniMax.getBestMove(board, maxDepth, true, new ArrayList<>());
        long endTime = System.currentTimeMillis();

        log.info("Minimax - MaxDepth = {}, Time Taken = {} (secs), BestScoreMoves = {}", maxDepth, (endTime - startTime)/1000.0, scoreMoves);

        return scoreMoves;
    }

    public static ScoreMoves getMiniMaxWithAlphaBetaResult(final Board board, final int maxDepth) {
        MiniMaxWithAlphaBeta.LEAF_NODES_EVALUATED = 0;
        long startTime = System.currentTimeMillis();

        ScoreMoves alpha = new ScoreMoves(Score.MIN_SCORE, new ArrayList<>());
        ScoreMoves beta = new ScoreMoves(Score.MAX_SCORE, new ArrayList<>());

        ScoreMoves scoreMoves = MiniMaxWithAlphaBeta.getBestMove(board, alpha, beta,
            maxDepth, true, new ArrayList<>());
        long endTime = System.currentTimeMillis();

        log.info("MiniMaxWithAlphaBeta - MaxDepth = {}, Time Taken = {} (secs), BestScoreMoves = {}", maxDepth, (endTime - startTime)/1000.0, scoreMoves);
        log.info("MiniMaxWithAlphaBeta - Evaluated leaf nodes = {}", MiniMaxWithAlphaBeta.LEAF_NODES_EVALUATED);

        MiniMaxWithAlphaBeta.LEAF_NODES_EVALUATED = 0;

        return scoreMoves;

    }

    private static ScoreMoves getMiniMaxWithAlphaBetaResult(final int maxDepth) {
        return getMiniMaxWithAlphaBetaResult(new Board.Builder().build(), maxDepth);
    }
}
