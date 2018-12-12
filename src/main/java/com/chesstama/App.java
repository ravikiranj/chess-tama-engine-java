package com.chesstama;

import com.chesstama.engine.Board;
import com.chesstama.engine.Card;
import com.chesstama.engine.PlayerType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(final String[] args) {
        Board board = new Board();
        log.info("Board = {}", board);

        for (PlayerType playerType : PlayerType.values()) {
            System.out.println("PlayerType = " + playerType);
            System.out.println("==============================");
            log.info("King Position = {}", board.getKingPosition(playerType));
            log.info("Pawn Positions = {}", board.getPawnPositions(playerType));
            log.info("Main Cards");
            System.out.println("==============================");
            for (Card card : board.getCards(playerType)) {
                card.printCard();
            }
            log.info("Upcoming Card");
            board.getUpcomingCard(playerType).printCard();
            System.out.println("==============================");
        }
    }
}
