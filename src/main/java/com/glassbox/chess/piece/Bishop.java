package com.glassbox.chess.piece;
import com.glassbox.chess.domain.*;
import java.util.ArrayList;
import java.util.List;
public class Bishop extends Piece {
public Bishop(Color color, Position position) { super(color, position, PieceType.BISHOP); }
@Override
public List<Move> getPseudoLegalMoves(Board board) {
List<Move> moves = new ArrayList<>();
int[][] dirs = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
for (int[] d : dirs) addSlidingMoves(board, moves, d[0], d[1]);
return moves;
}
}