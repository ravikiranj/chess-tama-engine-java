package com.chesstama;

import com.chesstama.engine.Board;
import com.chesstama.engine.PlayerType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(final String[] args) {
        Board board = new Board();
        log.info("Board = {}", board);

        for (PlayerType p : PlayerType.values()) {
            log.info("PlayerType = {}, KingPos = {}", p, board.getKingPosition(p));
        }

        for (PlayerType p : PlayerType.values()) {
            log.info("PlayerType = {}, PawnPos = {}", p, board.getPawnPositions(p));
        }
    }
}
