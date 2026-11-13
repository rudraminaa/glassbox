package com.glassbox.chess.piece;
import com.glassbox.chess.domain.*;
import com.glassbox.chess.rule.CheckDetector;
import java.util.ArrayList;
import java.util.List;
public class King extends Piece {
public King(Color color, Position position) { super(color, position, PieceType.KING); }
@Override
public List<Move> getPseudoLegalMoves(Board board) {
List<Move> moves = new ArrayList<>();
int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
for (int[] d : dirs) {
Position p = new Position(position.getFile() + d[0], position.getRank() + d[1]);
if (board.isValid(p)) {
Piece target = board.getPieceAt(p);
if (target == null || target.getColor() != this.color) {
moves.add(new Move(position, p, this, target));
}
}
}

// Castling Rules Logic
if (!hasMoved && position.getFile() == 4 && (position.getRank() == 0 || position.getRank() == 7)) {
if (canCastle(board, 1, 2)) {
Piece rook = board.getPieceAt(new Position(7, position.getRank()));
moves.add(new CastleMove(position, new Position(position.getFile() + 2, position.getRank()), this, rook,
new Position(7, position.getRank()), new Position(5, position.getRank())));
}
if (canCastle(board, -1, 3)) {
Piece rook = board.getPieceAt(new Position(0, position.getRank()));
moves.add(new CastleMove(position, new Position(position.getFile() - 2, position.getRank()), this, rook,
new Position(0, position.getRank()), new Position(3, position.getRank())));
}
}
return moves;
}
private boolean canCastle(Board board, int dir, int emptySquares) {
if (CheckDetector.isSquareAttacked(board, position, color.opposite())) return false; // Rule: Not in check
for (int i = 1; i <= emptySquares; i++) {
Position p = new Position(position.getFile() + i * dir, position.getRank());
if (board.getPieceAt(p) != null) return false; // Rule: Empty squares between
// Rule: King does not cross or land on an attacked square
if (i <= 2 && CheckDetector.isSquareAttacked(board, p, color.opposite())) return false;
}
Position rookPos = new Position(dir == 1 ? 7 : 0, position.getRank());
Piece rook = board.getPieceAt(rookPos);
return rook != null && rook.getType() == PieceType.ROOK && !rook.hasMoved() && rook.getColor() == color;
}
}