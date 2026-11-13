package com.glassbox.chess.domain;
import com.glassbox.chess.piece.Piece;
/**
* <p><b>Command Pattern:</b> Move classes now actively execute and undo themselves.</p>

* <p><b>Polymorphism:</b> Specialized move subclasses override execute/undo to handle side effects.</p>
*/
public class Move {
protected final Position from;
protected final Position to;
protected final Piece movedPiece;
protected final Piece capturedPiece;
protected final boolean wasFirstMove;
protected final boolean capturedWasFirstMove;
protected Position capturePos;
public Move(Position from, Position to, Piece movedPiece, Piece capturedPiece) {
this.from = from;
this.to = to;
this.movedPiece = movedPiece;
this.capturedPiece = capturedPiece;
this.wasFirstMove = !movedPiece.hasMoved();
this.capturedWasFirstMove = capturedPiece != null && !capturedPiece.hasMoved();
this.capturePos = to;
}
public void execute(Board board) {
board.removePiece(from);
if (capturedPiece != null) board.removePiece(capturePos);
board.placePiece(movedPiece, to);
movedPiece.setHasMoved(true);
}
public void undo(Board board) {
board.removePiece(to);
board.placePiece(movedPiece, from);
movedPiece.setHasMoved(!wasFirstMove);
if (capturedPiece != null) {
board.placePiece(capturedPiece, capturePos);
capturedPiece.setHasMoved(!capturedWasFirstMove);
}
}
public Position getFrom() { return from; }
public Position getTo() { return to; }
public Piece getMovedPiece() { return movedPiece; }
public Piece getCapturedPiece() { return capturedPiece; }
@Override
public String toString() { return from.toAlgebraic() + "-" + to.toAlgebraic(); }
}