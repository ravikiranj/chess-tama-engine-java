package com.chesstama.eval;

import com.chesstama.engine.Board;
import com.chesstama.engine.Card;
import com.chesstama.engine.Player;
import com.chesstama.engine.Position;
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
    private static final Position P1_KING_SLOT = new Position(4, 2);
    private static final Position P2_KING_SLOT = new Position(0, 2);

    private Evaluator() {
    }

    public static Score getBoardValue(final Board board) {
        Score totalScore = new Score();
        Set<Position> validMoves = getValidMoves(board);
        Set<Position> validOpponentMoves = getValidMovesForOpponent(board);

        computeKingScore(board, validMoves, totalScore);
        computePawnScore(board, validMoves, totalScore);
        computeMobilityScore(board, validMoves, totalScore);

        computeOpponentKingScore(board, validOpponentMoves, totalScore);
        computeOpponentPawnScore(board, validOpponentMoves, totalScore);

        return totalScore;
    }

    /**
     * compute opponent king threat score
     */
    private static void computeOpponentKingScore(final Board board,
                                                 final Set<Position> validOpponentMoves,
                                                 final Score score) {
        Player currentPlayer = board.getCurrentPlayer();
        Player opponent = currentPlayer.getOpponent();

        Position kingPos = board.getKingPosition(currentPlayer);
        Position kingHome = currentPlayer == Player.P1 ? P1_KING_SLOT : P2_KING_SLOT;
        List<Position> pawnPositions = board.getPawnPositions(currentPlayer);

        Position opponentKingPos = board.getKingPosition(opponent);

        // Opponent King
        for (Position validMove : validOpponentMoves) {
            Position potentialMovePos = opponentKingPos.add(validMove);
            if (!potentialMovePos.isValid()) {
                continue;
            }

            if (potentialMovePos.equals(kingPos)) {
                score.add(EvalRule.KING_CAPTURE);
            }

            if (potentialMovePos.equals(kingHome)) {
                score.add(EvalRule.KING_HOME);
            }

            if (pawnPositions.contains(potentialMovePos)) {
                score.add(EvalRule.PAWN_CAPTURE);
            }
        }
    }

    /**
     * compute opponent pawn threat score
     */
    private static void computeOpponentPawnScore(final Board board,
                                                 final Set<Position> validOpponentMoves,
                                                 final Score score) {
        Player currentPlayer = board.getCurrentPlayer();
        Player opponent = currentPlayer.getOpponent();

        Position kingPos = board.getKingPosition(currentPlayer);
        List<Position> pawnPositions = board.getPawnPositions(currentPlayer);

        List<Position> opponentPawnPositions = board.getPawnPositions(opponent);

        // Opponent Pawns
        for (Position opponentPawnPosition : opponentPawnPositions) {
            for (Position validMove : validOpponentMoves) {
                Position potentialMovePos = opponentPawnPosition.add(validMove);

                if (!potentialMovePos.isValid()) {
                    continue;
                }

                if (potentialMovePos.equals(kingPos)) {
                    score.add(EvalRule.KING_CAPTURE);
                }

                if (pawnPositions.contains(potentialMovePos)) {
                    score.add(EvalRule.PAWN_CAPTURE);
                }
            }
        }
    }

    /**
     * compute score reflecting unique reachable positions on the board
     */
    private static void computeMobilityScore(final Board board,
                                             final Set<Position> validMoves,
                                             final Score score) {
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

        score.add(EvalRule.UNIQUE_MOVE, uniqueValidMoves.size());
    }

    /**
     * 1) Opponent King Capture
     * 2) Opponent King Home Move
     * 3) Opponent Pawn Capture
     *
     * compute king's score reflecting above criteria
     */
    private static void computeKingScore(final Board board,
                                         final Set<Position> validMoves,
                                         final Score score) {
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
                score.add(EvalRule.OPP_KING_CAPTURE);
            }

            if (potentialMovePos.equals(opponentKingHome)) {
                score.add(EvalRule.OPP_KING_HOME);
            }

            if (pawnPositions.contains(potentialMovePos)) {
                score.add(EvalRule.OPP_PAWN_CAPTURE);
            }
        }
    }

    /**
     * 1) Opponent King Capture
     * 2) Opponent Pawn Capture
     *
     * compute pawns' score reflecting above criteria
     */
    private static void computePawnScore(final Board board,
                                         final Set<Position> validMoves,
                                         final Score score) {
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
                    score.add(EvalRule.OPP_KING_CAPTURE);
                }

                if (opponentPawnPositions.contains(potentialMovePos)) {
                    score.add(EvalRule.OPP_PAWN_CAPTURE);
                }
            }
        }
    }

    private static Set<Position> getValidMoves(final Board board) {
        Player currentPlayer = board.getCurrentPlayer();
        List<Card> cards = board.getCards(currentPlayer);

        return getValidMovesForPlayer(currentPlayer, cards);
    }

    private static Set<Position> getValidMovesForOpponent(final Board board) {
        Player opponent = board.getCurrentPlayer().getOpponent();
        List<Card> cards = board.getCards(opponent);

        return getValidMovesForPlayer(opponent, cards);
    }

    private static Set<Position> getValidMovesForPlayer(final Player player, final List<Card> cards) {
        return cards.stream()
                .map(card -> card.getRelativeMoves(player))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
