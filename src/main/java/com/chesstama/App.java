package com.chesstama;

import com.chesstama.engine.Board;
import com.chesstama.eval.Evaluator;
import com.chesstama.engine.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(final String[] args) {
        Board board = new Board.Builder()
                               .withCurrentPlayer(Player.P1)
                               .build();
        //log.info("Board = {}", board);
        board.printBoardOnly();

        log.info(
                "Current Player = {}, Board Value = {}",
                board.getCurrentPlayer(),
                Evaluator.getBoardValue(board)
        );

    }
}
