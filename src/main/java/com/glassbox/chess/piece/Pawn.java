package com.glassbox.chess.piece;
import com.glassbox.chess.domain.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
public Pawn(Color color, Position position) { super(color, position, PieceType.PAWN); }
@Override
public List<Move> getPseudoLegalMoves(Board board) {
List<Move> moves = new ArrayList<>();
int dir = (color == Color.WHITE) ? 1 : -1;
int promRank = (color == Color.WHITE) ? 7 : 0;
Position oneStep = new Position(position.getFile(), position.getRank() + dir);
if (board.isValid(oneStep) && board.getPieceAt(oneStep) == null) {
addMoveOrPromotions(moves, position, oneStep, null, promRank);
Position twoStep = new Position(position.getFile(), position.getRank() + 2 * dir);
if (position.getRank() == (color == Color.WHITE ? 1 : 6) && !hasMoved && board.isValid(twoStep) && board.getPieceAt(twoStep) == null) {
moves.add(new Move(position, twoStep, this, null));
}
}
for (int c : new int[]{-1, 1}) {
Position capPos = new Position(position.getFile() + c, position.getRank() + dir);
if (board.isValid(capPos)) {
Piece target = board.getPieceAt(capPos);
if (target != null && target.getColor() != this.color) {
addMoveOrPromotions(moves, position, capPos, target, promRank);
} else if (capPos.equals(board.getGameState().enPassantTarget)) {
Piece epPawn = board.getPieceAt(new Position(capPos.getFile(), position.getRank()));
moves.add(new EnPassantMove(position, capPos, this, epPawn));
}
}
}
return moves;
}
private void addMoveOrPromotions(List<Move> moves, Position from, Position to, Piece cap, int promRank) {
if (to.getRank() == promRank) {
moves.add(new PromotionMove(from, to, this, cap, PieceType.QUEEN));
moves.add(new PromotionMove(from, to, this, cap, PieceType.ROOK));
moves.add(new PromotionMove(from, to, this, cap, PieceType.BISHOP));
moves.add(new PromotionMove(from, to, this, cap, PieceType.KNIGHT));
} else {
moves.add(new Move(from, to, this, cap));
}
}
}