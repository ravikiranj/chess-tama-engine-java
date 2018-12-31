package com.chesstama.testers;

import com.chesstama.engine.Board;
import com.chesstama.eval.MiniMax;
import com.chesstama.eval.ScoreMoves;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class MiniMaxTester {
    public static void main(final String[] args) {
        int maxDepth = 3;
        Board board = new Board.Builder().build();
        Board boardCopy = board.copy();

        long startTime = System.currentTimeMillis();
        ScoreMoves scoreMoves = MiniMax.getBestMove(board, maxDepth, true, new ArrayList<>());
        long endTime = System.currentTimeMillis();

        MoveTester.makeMoves(boardCopy, scoreMoves.getMoves());

        log.info("MaxDepth = {}, Time Taken = {} (secs), BestScoreMoves = {}", maxDepth, (endTime - startTime)/1000.0, scoreMoves);

    }
}
