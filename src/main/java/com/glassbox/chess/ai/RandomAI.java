package com.glassbox.chess.ai;
import com.glassbox.chess.domain.Board;
import com.glassbox.chess.domain.Move;
import com.glassbox.chess.rule.MoveValidator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
public class RandomAI implements AIStrategy {
private final Random random = new Random();
@Override
public Move calculateMove(Board board, Supplier<Boolean> isCancelled) {
List<Move> legals = MoveValidator.getLegalMoves(board, board.getGameState().turn);
if (legals.isEmpty()) return null;
// Slight preference for captures
List<Move> captures = legals.stream().filter(m -> m.getCapturedPiece() != null).toList();
if (!captures.isEmpty() && random.nextBoolean()) {
return captures.get(random.nextInt(captures.size()));
}
return legals.get(random.nextInt(legals.size()));
}
}