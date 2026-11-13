package com.glassbox.chess.piece;
import com.glassbox.chess.domain.Color;
import com.glassbox.chess.domain.PieceType;
import com.glassbox.chess.domain.Position;

/**
* <p><b>Factory Pattern:</b> Centralizes piece instantiation.</p>
*/
public class PieceFactory {
public static Piece createPiece(PieceType type, Color color, Position position) {
switch (type) {
case PAWN: return new Pawn(color, position);
case KNIGHT: return new Knight(color, position);
case BISHOP: return new Bishop(color, position);
case ROOK: return new Rook(color, position);
case QUEEN: return new Queen(color, position);
case KING: return new King(color, position);
default: throw new IllegalArgumentException("Unknown piece type");
}
}
}