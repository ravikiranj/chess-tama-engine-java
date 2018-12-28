package com.chesstama.eval;

import com.chesstama.engine.Board;
import com.chesstama.engine.Card;
import com.chesstama.engine.Player;
import com.chesstama.engine.Position;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates"})
@Slf4j
public final class Evaluator {
    public static final int BASE_SCORE = 1;

    private Evaluator() {
    }

    public static Score getBoardValue(final Board board) {
        if (board.isGameOver()) {
            Player winner = board.getGameWinner().get();
            return winner == board.getCurrentPlayer() ? Score.MAX_SCORE : Score.MIN_SCORE;
        }

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
     * 1) King capture
     * 2) Reach King home
     * 3) Pawn capture
     *
     * compute opponent king threat score
     */
    private static void computeOpponentKingScore(final Board board,
                                                 final Set<Position> validOpponentMoves,
                                                 final Score score) {
        Player currentPlayer = board.getCurrentPlayer();
        Player opponent = currentPlayer.getOpponent();

        Position kingPos = board.getKingPosition(currentPlayer);
        Position kingHome = currentPlayer == Player.P1 ? Board.P1_KING_SLOT : Board.P2_KING_SLOT;
        Set<Position> pawnPositions = board.getPawnPositions(currentPlayer);

        Position opponentKingPos = board.getKingPosition(opponent);
        Set<Position> opponentPawnPositions = board.getPawnPositions(opponent);
        Set<Position> opponentPiecePositions = new ImmutableSet.Builder<Position>()
                .add(opponentKingPos)
                .addAll(opponentPawnPositions)
                .build();

        // Opponent King
        for (Position validMove : validOpponentMoves) {
            Position potentialMovePos = opponentKingPos.add(validMove);

            if (!isValidPosition(potentialMovePos, opponentPiecePositions)) {
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
     * 1) King capture
     * 2) Pawn capture
     *
     * compute opponent pawn threat score
     */
    private static void computeOpponentPawnScore(final Board board,
                                                 final Set<Position> validOpponentMoves,
                                                 final Score score) {
        Player currentPlayer = board.getCurrentPlayer();
        Player opponent = currentPlayer.getOpponent();

        Position kingPos = board.getKingPosition(currentPlayer);
        Set<Position> pawnPositions = board.getPawnPositions(currentPlayer);

        Position opponentKingPos = board.getKingPosition(opponent);
        Set<Position> opponentPawnPositions = board.getPawnPositions(opponent);
        Set<Position> opponentPiecePositions = new ImmutableSet.Builder<Position>()
                .add(opponentKingPos)
                .addAll(opponentPawnPositions)
                .build();

        // Opponent Pawns
        for (Position opponentPawnPosition : opponentPawnPositions) {
            for (Position validMove : validOpponentMoves) {
                Position potentialMovePos = opponentPawnPosition.add(validMove);

                if (!isValidPosition(potentialMovePos, opponentPiecePositions)) {
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

        Set<Position> piecePositions = new ImmutableSet.Builder<Position>()
            .add(board.getKingPosition(currentPlayer))
            .addAll(board.getPawnPositions(currentPlayer))
            .build();

        Set<Position> uniqueValidMoves = new HashSet<>();

        for (Position piecePosition : piecePositions) {
            for (Position validMove : validMoves) {
                Position potentialMovePos = piecePosition.add(validMove);

                if (isValidPosition(potentialMovePos, piecePositions)) {
                    uniqueValidMoves.add(potentialMovePos);
                }
            }
        }

        log.info("Unique Valid Moves = {}", uniqueValidMoves);

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
        Set<Position> pawnPositions = board.getPawnPositions(currentPlayer);
        Set<Position> piecePositions = new ImmutableSet.Builder<Position>()
                .add(kingPos)
                .addAll(pawnPositions)
                .build();

        Position opponentKingPos = board.getKingPosition(opponent);
        Position opponentKingHome = currentPlayer == Player.P1 ? Board.P2_KING_SLOT : Board.P1_KING_SLOT;
        Set<Position> opponentPawnPositions = new HashSet<>(board.getPawnPositions(opponent));

        for (Position validMove : validMoves) {
            Position potentialMovePos = kingPos.add(validMove);

            if (!isValidPosition(potentialMovePos, piecePositions)) {
                continue;
            }

            if (potentialMovePos.equals(opponentKingPos)) {
                score.add(EvalRule.OPP_KING_CAPTURE);
            }

            if (potentialMovePos.equals(opponentKingHome)) {
                score.add(EvalRule.OPP_KING_HOME);
            }

            if (opponentPawnPositions.contains(potentialMovePos)) {
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

        Position kingPos = board.getKingPosition(currentPlayer);
        Set<Position> pawnPositions = board.getPawnPositions(currentPlayer);
        Set<Position> piecePositions = new ImmutableSet.Builder<Position>()
                .add(kingPos)
                .addAll(pawnPositions)
                .build();

        Position opponentKingPos = board.getKingPosition(opponent);
        Set<Position> opponentPawnPositions = new HashSet<>(board.getPawnPositions(opponent));

        for (Position pawnPosition : pawnPositions) {
            for (Position validMove : validMoves) {
                Position potentialMovePos = pawnPosition.add(validMove);

                if (!isValidPosition(potentialMovePos, piecePositions)) {
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

    private static boolean isValidPosition(final Position position,
                                           final Set<Position> piecePositions) {
        return position.isValid() && !piecePositions.contains(position);
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
