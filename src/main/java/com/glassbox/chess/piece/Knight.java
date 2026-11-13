package com.glassbox.chess.piece;
import com.glassbox.chess.domain.*;
import java.util.ArrayList;
import java.util.List;
public class Knight extends Piece {

private static final int[][] JUMPS = {{1, 2}, {2, 1}, {-1, 2}, {-2, 1}, {1, -2}, {2, -1}, {-1, -2}, {-2, -1}};
public Knight(Color color, Position position) { super(color, position, PieceType.KNIGHT); }
@Override
public List<Move> getPseudoLegalMoves(Board board) {
List<Move> moves = new ArrayList<>();
for (int[] jump : JUMPS) {
Position p = new Position(position.getFile() + jump[0], position.getRank() + jump[1]);
if (board.isValid(p)) {
Piece target = board.getPieceAt(p);
if (target == null || target.getColor() != this.color) {
moves.add(new Move(position, p, this, target));
}
}
}
return moves;
}
}