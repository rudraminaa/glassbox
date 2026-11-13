package com.glassbox.chess.ai;
import com.glassbox.chess.domain.*;
import com.glassbox.chess.rule.GameStateEvaluator;
import com.glassbox.chess.piece.Piece;
import com.glassbox.chess.piece.Piece;
import com.glassbox.chess.piece.Piece;
import com.glassbox.chess.piece.Piece;
import com.glassbox.chess.piece.Piece;
import com.glassbox.chess.piece.Piece;
public class StandardEvaluator implements BoardEvaluator {
private static final int MATE_SCORE = 100000;
// Simplified Piece-Square Table (encourages center control)
private static final int[][] CENTER_BONUS = {
{ 0, 0, 0, 0, 0, 0, 0, 0},
{ 0, 0, 0, 0, 0, 0, 0, 0},
{ 0, 0, 10, 10, 10, 10, 0, 0},
{ 0, 0, 10, 20, 20, 10, 0, 0},
{ 0, 0, 10, 20, 20, 10, 0, 0},
{ 0, 0, 10, 10, 10, 10, 0, 0},
{ 0, 0, 0, 0, 0, 0, 0, 0},
{ 0, 0, 0, 0, 0, 0, 0, 0}
};
@Override
public int evaluate(Board board, Color perspective) {
GameStatus status = GameStateEvaluator.evaluate(board);
if (status == GameStatus.CHECKMATE) {
return board.getGameState().turn == perspective ? -MATE_SCORE : MATE_SCORE;
}

if (status == GameStatus.STALEMATE || status == GameStatus.DRAW) return 0;
int score = 0;
for (int f = 0; f < 8; f++) {
for (int r = 0; r < 8; r++) {
Piece p = board.getPieceAt(new Position(f, r));
if (p != null) {
int val = getPieceValue(p.getType()) + CENTER_BONUS[r][f];
score += (p.getColor() == perspective) ? val : -val;
}
}
}
return score;
}
private int getPieceValue(PieceType type) {
return switch (type) {
case PAWN -> 100; case KNIGHT, BISHOP -> 300;
case ROOK -> 500; case QUEEN -> 900; case KING -> 0;
};
}
}