package com.glassbox.chess.domain;
import com.glassbox.chess.piece.Piece;
/**
* Represents a single board square.
* <p><b>Aggregation:</b> Holds a reference to a Piece, but the Piece can exist outside the Square.</p>
*/
public class Square {
private final Position position;
private Piece piece;

public Square(Position position) {
this.position = position;
}
public Position getPosition() { return position; }
public Piece getPiece() { return piece; }
public void setPiece(Piece piece) { this.piece = piece; }
public boolean isEmpty() { return piece == null; }
}