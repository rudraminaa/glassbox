package com.glassbox.chess.domain;
import com.glassbox.chess.piece.Piece;
public class CastleMove extends Move {
private final Piece rook;
private final Position rookFrom;
private final Position rookTo;
public CastleMove(Position kFrom, Position kTo, Piece king, Piece rook, Position rFrom, Position rTo) {
super(kFrom, kTo, king, null);
this.rook = rook;
this.rookFrom = rFrom;
this.rookTo = rTo;
}
@Override
public void execute(Board board) {
super.execute(board);
Piece r = board.removePiece(rookFrom);
board.placePiece(r, rookTo);
rook.setHasMoved(true);
}
@Override

public void undo(Board board) {
super.undo(board);
Piece r = board.removePiece(rookTo);
board.placePiece(r, rookFrom);
rook.setHasMoved(false); // Castling strictly implies rook hadn't moved
}
}