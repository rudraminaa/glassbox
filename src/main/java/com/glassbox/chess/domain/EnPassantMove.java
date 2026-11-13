package com.glassbox.chess.domain;
import com.glassbox.chess.piece.Piece;
public class EnPassantMove extends Move {
public EnPassantMove(Position from, Position to, Piece movedPiece, Piece capturedPiece) {
super(from, to, movedPiece, capturedPiece);
// In En Passant, the pawn is captured on the rank of the attacking pawn
this.capturePos = new Position(to.getFile(), from.getRank());
}
}