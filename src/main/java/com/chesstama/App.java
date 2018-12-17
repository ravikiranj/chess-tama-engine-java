package com.chesstama;

import com.chesstama.engine.Board;
import com.chesstama.engine.Evaluator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(final String[] args) {
        Board board = new Board.Builder()
                               .build();
        log.info("Board = {}", board);
        board.printBoardState();

        log.info(
                "Current Player = {}, Board Value = {}",
                board.getCurrentPlayer(),
                Evaluator.getBoardValue(board)
        );

    }
}
