package com.glassbox.chess.ai;
import com.glassbox.chess.domain.Board;
import com.glassbox.chess.domain.Move;
import com.glassbox.chess.rule.MoveValidator;
import java.util.List;
import java.util.function.Supplier;
public class GreedyAI implements AIStrategy {
private final BoardEvaluator evaluator = new StandardEvaluator();
@Override
public Move calculateMove(Board board, Supplier<Boolean> isCancelled) {
List<Move> legals = MoveValidator.getLegalMoves(board, board.getGameState().turn);
Move bestMove = null;
int bestScore = Integer.MIN_VALUE;
for (Move move : legals) {
if (isCancelled.get()) return null;
board.applyMove(move);

int score = -evaluator.evaluate(board, board.getGameState().turn); // Evaluate from opponent's view and nega
board.undoMove(move);
if (score > bestScore) {
bestScore = score;
bestMove = move;
}
}
return bestMove;
}
}