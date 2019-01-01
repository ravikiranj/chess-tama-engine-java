package com.chesstama.backend.eval;

import com.chesstama.backend.engine.Board;
import com.chesstama.backend.engine.Player;
import com.chesstama.backend.engine.Position;
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
     * Reference:  https://en.wikipedia.org/wiki/Minimax
     *
     * @param board Board
     * @param maxDepth Max Depth
     * @param isMaximizingPlayer Flag representing if current turn is of maximizing player
     * @param currentMovePath Current Move Path
     * @return Best Score along with Move Path
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
