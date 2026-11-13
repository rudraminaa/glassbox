package com.glassbox.chess.rule;
import com.glassbox.chess.domain.Board;
import com.glassbox.chess.domain.Color;
import com.glassbox.chess.domain.Move;
import com.glassbox.chess.domain.Position;
import com.glassbox.chess.piece.Piece;
import java.util.ArrayList;
import java.util.List;
public class MoveValidator {
/**
* Filters pseudo-legal moves into strictly legal moves by simulating each
* move and verifying it does not leave the moving player's king in check.
*/
public static List<Move> getLegalMoves(Board board, Color color) {
List<Move> pseudoMoves = new ArrayList<>();
for (int f = 0; f < 8; f++) {
for (int r = 0; r < 8; r++) {
Piece piece = board.getPieceAt(new Position(f, r));
if (piece != null && piece.getColor() == color) {
pseudoMoves.addAll(piece.getPseudoLegalMoves(board));
}
}
}
List<Move> legalMoves = new ArrayList<>();
for (Move move : pseudoMoves) {
board.applyMove(move);
if (!CheckDetector.isInCheck(board, color)) {
legalMoves.add(move);
}
board.undoMove(move);
}
return legalMoves;
}
}