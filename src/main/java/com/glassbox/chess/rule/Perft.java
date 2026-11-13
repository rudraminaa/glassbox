package com.glassbox.chess.rule;
import com.glassbox.chess.domain.Board;
import com.glassbox.chess.domain.Move;
import java.util.List;
public class Perft {
public static long explore(Board board, int depth) {
if (depth == 0) return 1;
List<Move> moves = MoveValidator.getLegalMoves(board, board.getGameState().turn);
if (depth == 1) return moves.size();
long nodes = 0;
for (Move move : moves) {
board.applyMove(move);

nodes += explore(board, depth - 1);
board.undoMove(move);
}
return nodes;
}
}