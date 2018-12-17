package com.chesstama.engine;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates"})
public final class Evaluator {
    private static final int OPP_KING_CAPTURE = Integer.MAX_VALUE;
    private static final int OPP_KING_HOME_MOVE = Integer.MAX_VALUE;
    private static final int OPP_PAWN_CAPTURE = Byte.MAX_VALUE;

    private static final Position P1_KING_SLOT = new Position(4, 2);
    private static final Position P2_KING_SLOT = new Position(0, 2);

    private Evaluator() {
    }

    public static long getBoardValue(final Board board) {
        long value = 0L;
        List<Position> validMoves = getValidMoves(board);

        value += getKingScore(board, validMoves);
        value += getPawnsScore(board, validMoves);

        return value;
    }

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
