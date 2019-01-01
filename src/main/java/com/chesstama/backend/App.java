package com.chesstama.backend;

import com.chesstama.backend.engine.Board;
import com.chesstama.backend.eval.Evaluator;
import com.chesstama.backend.generators.BoardGenerator;
import lombok.extern.slf4j.Slf4j;

import static com.chesstama.backend.generators.BoardGenerator.ET;
import static com.chesstama.backend.generators.BoardGenerator.K1;
import static com.chesstama.backend.generators.BoardGenerator.K2;
import static com.chesstama.backend.generators.BoardGenerator.P1;
import static com.chesstama.backend.generators.BoardGenerator.P2;

@Slf4j
public class App {
    public static void main(final String[] args) {
        String[][] board = new String[][]{
            {
                    P2, ET, ET, K1, P2
            },
            {
                    ET, ET, K2, ET, ET
            },
            {
                    ET, P2, P1, P2, ET
            },
            {
                    ET, P1, ET, P1, ET
            },
            {
                    P1, ET, ET, ET, ET
            },
        };
        Board chessTamaBoard = BoardGenerator.getBoard(board);
        log.info("Board = {}", chessTamaBoard);
        chessTamaBoard.printBoardState();

        log.info(
                "Current Player = {}, Score Details = {}",
                chessTamaBoard.getCurrentPlayer(),
                Evaluator.getBoardValue(chessTamaBoard)
        );

    }
}
