package com.chesstama.eval;

import com.chesstama.engine.Board;
import com.chesstama.engine.Player;
import com.chesstama.engine.Position;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public final class MiniMax {

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
    public static ScoreMoves getBestMove(Board board,
                                         final int maxDepth,
                                         final boolean isMaximizingPlayer,
                                         final List<Move> currentMovePath) {
        if (maxDepth == 0 || board.isGameOver()) {
            Score score = Evaluator.getBoardValue(board);
            log.info("Score = {}, Max Depth = {}, isGameOver = {}, Score = {}", score, maxDepth, board.isGameOver());

            return new ScoreMoves(score, new ArrayList<>(currentMovePath));
        }

        Player currentPlayer = board.getCurrentPlayer();
        Set<PiecePosition> piecePositions = board.getAllPiecePositions(currentPlayer);
        Set<Position> rawPiecePositions = piecePositions.stream()
                                                        .map(PiecePosition::getPosition)
                                                        .collect(Collectors.toSet());
        Set<CardMove> cardMoves = board.getAllCardMoves(currentPlayer);

        if (isMaximizingPlayer) {
            ScoreMoves scoreMoves = new ScoreMoves(Score.MIN_SCORE, new ArrayList<>());

            for (PiecePosition piecePosition : piecePositions) {
                for (CardMove cardMove : cardMoves) {
                    Position from = piecePosition.getPosition();
                    Position to = from.add(cardMove.getRelativeMove());

                    if (!isValidMove(rawPiecePositions, from, to)) {
                        continue;
                    }

                    // Compute move
                    Move move = new Move(
                        currentPlayer,
                        cardMove.getCard(),
                        piecePosition.getPieceType(),
                        from,
                        to
                    );

                    // Save board state
                    Board boardCopy = board.copy();

                    // Make move
                    board.makeMove(move);

                    // Add move to current path
                    currentMovePath.add(move);

                    // Compute score
                    ScoreMoves currentScoreMoves = getBestMove(board, maxDepth-1, false, currentMovePath);

                    // Remove move from current path
                    currentMovePath.remove(currentMovePath.size() - 1);

                    // Update scoreMoves if applicable
                    if (currentScoreMoves.compareTo(scoreMoves) > 0) {
                        scoreMoves = currentScoreMoves;
                    }

                    // Restore board state
                    board = boardCopy;
                }
            }

            return scoreMoves;
        } else {
            ScoreMoves scoreMoves = new ScoreMoves(Score.MAX_SCORE, new ArrayList<>());

            for (PiecePosition piecePosition : piecePositions) {
                for (CardMove cardMove : cardMoves) {
                    Position from = piecePosition.getPosition();
                    Position to = from.add(cardMove.getRelativeMove());

                    if (!isValidMove(rawPiecePositions, from, to)) {
                        continue;
                    }

                    // Compute move
                    Move move = new Move(
                        currentPlayer,
                        cardMove.getCard(),
                        piecePosition.getPieceType(),
                        from,
                        to
                    );

                    // Save board state
                    Board boardCopy = board.copy();

                    // Make move
                    board.makeMove(move);

                    // Add current move to path
                    currentMovePath.add(move);

                    // Compute score
                    ScoreMoves currentScoreMoves = getBestMove(board, maxDepth-1, true, currentMovePath);

                    // Remove current move from path
                    currentMovePath.remove(currentMovePath.size()-1);

                    // Update scoreMoves if applicable
                    if (currentScoreMoves.compareTo(scoreMoves) < 0) {
                        scoreMoves = currentScoreMoves;
                    }

                    // Restore board state
                    board = boardCopy;
                }
            }

            return scoreMoves;
        }
    }

    private static boolean isValidMove(final Set<Position> rawPiecePositions,
                                       final Position from,
                                       final Position to) {
        return from.isValid() && to.isValid() && !rawPiecePositions.contains(to);
    }

}
