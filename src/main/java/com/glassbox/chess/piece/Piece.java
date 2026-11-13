package com.glassbox.chess.piece;
import com.glassbox.chess.domain.Board;
import com.glassbox.chess.domain.Color;
import com.glassbox.chess.domain.Move;
import com.glassbox.chess.domain.Position;
import com.glassbox.chess.domain.PieceType;
import java.util.List;
/**
* Base abstract class for all chess pieces.
* <p><b>Abstraction:</b> Defines the getPseudoLegalMoves contract.</p>
*/
public abstract class Piece {
protected final Color color;
protected Position position;
protected boolean hasMoved;
protected final PieceType type;
protected Piece(Color color, Position position, PieceType type) {
this.color = color;
this.position = position;
this.type = type;
this.hasMoved = false;
}
public Color getColor() { return color; }
public Position getPosition() { return position; }
public void setPosition(Position position) { this.position = position; }
public boolean hasMoved() { return hasMoved; }
public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
public PieceType getType() { return type; }
public abstract List<Move> getPseudoLegalMoves(Board board);
/** Helper for sliding pieces (Queen, Rook, Bishop) */
protected void addSlidingMoves(Board board, List<Move> moves, int dFile, int dRank) {
int f = position.getFile() + dFile;
int r = position.getRank() + dRank;
while (true) {
Position p = new Position(f, r);
if (!board.isValid(p)) break;
Piece target = board.getPieceAt(p);
if (target == null) {
moves.add(new Move(position, p, this, null));
} else {
if (target.getColor() != this.color) {
moves.add(new Move(position, p, this, target));
}
break;
}
f += dFile;
r += dRank;
}
}
}