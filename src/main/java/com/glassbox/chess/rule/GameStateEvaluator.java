package com.glassbox.chess.rule;
import com.glassbox.chess.domain.Board;
import com.glassbox.chess.domain.Color;
import com.glassbox.chess.domain.GameStatus;
import com.glassbox.chess.domain.Move;
import java.util.List;
public class GameStateEvaluator {
public static GameStatus evaluate(Board board) {
Color turn = board.getGameState().turn;
List<Move> legalMoves = MoveValidator.getLegalMoves(board, turn);
boolean inCheck = CheckDetector.isInCheck(board, turn);
if (legalMoves.isEmpty()) return inCheck ? GameStatus.CHECKMATE : GameStatus.STALEMATE;
if (board.getGameState().halfMoveClock >= 100) return GameStatus.DRAW; // 50-move rule
if (board.isInsufficientMaterial()) return GameStatus.DRAW;
if (board.getRepetitionCount(board.getPositionKey()) >= 3) return GameStatus.DRAW;
return inCheck ? GameStatus.CHECK : GameStatus.ACTIVE;
}
}