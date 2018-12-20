package com.chesstama.engine;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates"})
@Slf4j
public final class Evaluator {
    private static final int BASE_SCORE = 10;

    private static final int OPP_KING_CAPTURE = BASE_SCORE * 1000;
    private static final int OPP_KING_HOME_MOVE = BASE_SCORE * 1000;

    private static final int OPP_PAWN_CAPTURE = BASE_SCORE * 100;

    private static final int UNIQUE_MOVE = BASE_SCORE * 25;

    private static final Position P1_KING_SLOT = new Position(4, 2);
    private static final Position P2_KING_SLOT = new Position(0, 2);

    private Evaluator() {
    }

    public static long getBoardValue(final Board board) {
        long value = 0L;
        List<Position> validMoves = getValidMoves(board);

        value += getKingScore(board, validMoves);
        value += getPawnsScore(board, validMoves);
        value += getMobilityScore(board, validMoves);

        return value;
    }

    /**
     * @return score reflecting unique reachable positions on the board
     */
    private static long getMobilityScore(final Board board, final List<Position> validMoves) {
        Player currentPlayer = board.getCurrentPlayer();

        List<Position> piecePositions = new ImmutableList.Builder<Position>()
            .add(board.getKingPosition(currentPlayer))
            .addAll(board.getPawnPositions(currentPlayer))
            .build();

        Set<Position> uniqueValidMoves = new HashSet<>();

        for (Position piecePosition : piecePositions) {
            for (Position validMove : validMoves) {
                Position potentialMovePos = piecePosition.add(validMove);
                if (potentialMovePos.isValid() && !uniqueValidMoves.contains(potentialMovePos)) {
                    uniqueValidMoves.add(potentialMovePos);
                }
            }
        }

        long score = uniqueValidMoves.size() * UNIQUE_MOVE;

        log.info("Unique Valid Moves = {}, Count = {}, Score = {}",
            uniqueValidMoves, uniqueValidMoves.size(), score);

        return score;
    }

    /**
     * 1) Opponent King Capture
     * 2) Opponent King Home Move
     * 3) Opponent Pawn Capture
     *
     * @return king's score reflecting above criteria
     */
    private static long getKingScore(final Board board,
                                     final List<Position> validMoves) {
        long score = 0L;
        Player currentPlayer = board.getCurrentPlayer();
        Player opponent = currentPlayer.getOpponent();

        Position kingPos = board.getKingPosition(currentPlayer);

        Position opponentKingPos = board.getKingPosition(opponent);
        Position opponentKingHome = currentPlayer == Player.P1 ? P2_KING_SLOT : P1_KING_SLOT;
        Set<Position> pawnPositions = new HashSet<>(board.getPawnPositions(opponent));

        for (Position validMove : validMoves) {
            Position potentialMovePos = kingPos.add(validMove);
            if (!potentialMovePos.isValid()) {
                continue;
            }

            if (potentialMovePos.equals(opponentKingPos)) {
                score += OPP_KING_CAPTURE;
            }

            if (potentialMovePos.equals(opponentKingHome)) {
                score += OPP_KING_HOME_MOVE;
            }

            if (pawnPositions.contains(potentialMovePos)) {
                score += OPP_PAWN_CAPTURE;
            }
        }

        return score;
    }

    /**
     * 1) Opponent King Capture
     * 2) Opponent Pawn Capture
     *
     * @return pawns' score reflecting above criteria
     */
    private static long getPawnsScore(final Board board,
                                      final List<Position> validMoves) {
        long score = 0L;
        Player currentPlayer = board.getCurrentPlayer();
        Player opponent = currentPlayer.getOpponent();

        List<Position> pawnPositions = board.getPawnPositions(currentPlayer);

        Position opponentKingPos = board.getKingPosition(opponent);
        Set<Position> opponentPawnPositions = new HashSet<>(board.getPawnPositions(opponent));

        for (Position pawnPosition : pawnPositions) {
            for (Position validMove : validMoves) {
                Position potentialMovePos = pawnPosition.add(validMove);
                if (!potentialMovePos.isValid()) {
                    continue;
                }

                if (potentialMovePos.equals(opponentKingPos)) {
                    score += OPP_KING_CAPTURE;
                }

                if (opponentPawnPositions.contains(potentialMovePos)) {
                    score += OPP_PAWN_CAPTURE;
                }
            }
        }

        return score;
    }

    private static List<Position> getValidMoves(final Board board) {
        Player currentPlayer = board.getCurrentPlayer();
        List<Card> cards = board.getCards(currentPlayer);

        return cards.stream()
                    .map(card -> card.getRelativeMoves(currentPlayer))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
    }
}
