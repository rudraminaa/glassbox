package com.glassbox.chess.domain;
import com.glassbox.chess.piece.Piece;
import com.glassbox.chess.piece.PieceFactory;
public class PromotionMove extends Move {
private final PieceType promotionType;
private Piece promotedPiece;
public PromotionMove(Position from, Position to, Piece movedPiece, Piece capturedPiece, PieceType promotionType) {
super(from, to, movedPiece, capturedPiece);
this.promotionType = promotionType;
}
public PieceType getPromotionType() { return promotionType; }

@Override
public void execute(Board board) {
super.execute(board);
board.removePiece(to);
promotedPiece = PieceFactory.createPiece(promotionType, movedPiece.getColor(), to);
promotedPiece.setHasMoved(true);
board.placePiece(promotedPiece, to);
}
@Override
public void undo(Board board) {
board.removePiece(to);
board.placePiece(movedPiece, from);
movedPiece.setHasMoved(!wasFirstMove);
if (capturedPiece != null) {
board.placePiece(capturedPiece, capturePos);
capturedPiece.setHasMoved(!capturedWasFirstMove);
}
}
}