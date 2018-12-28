package com.chesstama.eval;

import com.chesstama.engine.Board;
import com.chesstama.engine.Player;
import com.chesstama.engine.Position;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public final class MiniMax {

    private static final String BAR_SEPARATOR = "=======================";

    private MiniMax() {
    }

    /**
     * function minimax(node, depth, maximizingPlayer) is
     *     if depth = 0 or node is a terminal node then
     *         return the heuristic value of node
     *     if maximizingPlayer then
     *         value := −∞
     *         for each child of node do
     *             value := max(value, minimax(child, depth − 1, FALSE))
     *         return value
     *     else (* minimizing player *)
     *         value := +∞
     *         for each child of node do
     *             value := min(value, minimax(child, depth − 1, TRUE))
     *         return value
     *
     * @param board
     * @param maxDepth
     * @param isMaximizingPlayer
     * @return
     */
    @SuppressWarnings({"Duplicates", "PMD.AvoidReassigningParameters", "PMD.AvoidInstantiatingObjectsInLoops"})
    public static Score getBestMove(Board board,
                                    final int maxDepth,
                                    final boolean isMaximizingPlayer) {
        if (maxDepth == 0 || board.isGameOver()) {
            log.info("Max Depth = {}, isGameOver = {}", maxDepth, board.isGameOver());
            return Evaluator.getBoardValue(board);
        }

        Player currentPlayer = board.getCurrentPlayer();
        Set<PiecePosition> piecePositions = board.getAllPiecePositions(currentPlayer);
        Set<Position> rawPiecePositions = piecePositions.stream()
                                                        .map(PiecePosition::getPosition)
                                                        .collect(Collectors.toSet());
        Set<CardMove> cardMoves = board.getAllCardMoves(currentPlayer);

        if (isMaximizingPlayer) {
            Score score = new Score(Long.MIN_VALUE);

            for (PiecePosition piecePosition : piecePositions) {
                for (CardMove cardMove : cardMoves) {
                    Position from = piecePosition.getPosition();
                    Position to = from.add(cardMove.getRelativeMove());

                    if (!isValidMove(rawPiecePositions, from, to)) {
                        continue;
                    }

                    Move move = new Move(
                        currentPlayer,
                        cardMove.getCard(),
                        piecePosition.getPieceType(),
                        from,
                        to
                    );

                    // Save board state
                    Board boardCopy = board.copy();

                    log.info("About to make move = {}", move);
                    board.printBoardOnly();

                    board.makeMove(move);
                    printBoardMoveScore(board, move, score);
                    score = Score.max(score, getBestMove(board, maxDepth-1, false));

                    // Restore board state
                    board = boardCopy;
                }
            }

            return score;
        } else {
            Score score = new Score(Long.MAX_VALUE);

            for (PiecePosition piecePosition : piecePositions) {
                for (CardMove cardMove : cardMoves) {
                    Position from = piecePosition.getPosition();
                    Position to = from.add(cardMove.getRelativeMove());

                    if (!isValidMove(rawPiecePositions, from, to)) {
                        continue;
                    }

                    Move move = new Move(
                        currentPlayer,
                        cardMove.getCard(),
                        piecePosition.getPieceType(),
                        from,
                        to
                    );

                    // Save board state
                    Board boardCopy = board.copy();

                    board.makeMove(move);
                    printBoardMoveScore(board, move, score);
                    score = Score.min(score, getBestMove(board, maxDepth-1, true));

                    // Restore board state
                    board = boardCopy;
                }
            }

            return score;
        }
    }

    private static boolean isValidMove(final Set<Position> rawPiecePositions,
                                       final Position from,
                                       final Position to) {
        return from.isValid() && to.isValid() && !rawPiecePositions.contains(to);
    }

    private static void printBoardMoveScore(final Board board,
                                            final Move move,
                                            final Score score) {
        System.out.println("\n");
        System.out.println(BAR_SEPARATOR);
        System.out.println("Print Board Start");
        System.out.println(BAR_SEPARATOR);
        log.info("Score = {}, Move = {}", score, move);
        System.out.println("\n");
        board.printBoardOnly();
        System.out.println(BAR_SEPARATOR);
        System.out.println("Print Board End");
        System.out.println(BAR_SEPARATOR);
    }
}
