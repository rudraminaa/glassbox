package com.glassbox.chess.util;
import com.glassbox.chess.domain.*;
import com.glassbox.chess.piece.Piece;
import com.glassbox.chess.piece.PieceFactory;
public class FenParser {
public static Board parse(String fen) {
Board board = new Board();
String[] parts = fen.split(" ");
int rank = 7, file = 0;
for (char c : parts[0].toCharArray()) {
if (c == '/') { rank--; file = 0; }
else if (Character.isDigit(c)) { file += Character.getNumericValue(c); }
else {
Color col = Character.isUpperCase(c) ? Color.WHITE : Color.BLACK;
PieceType pt = charToType(c);
board.placePiece(PieceFactory.createPiece(pt, col, new Position(file, rank)), new Position(file, rank));
file++;
}
}
Color turn = parts[1].equals("w") ? Color.WHITE : Color.BLACK;
String castling = parts[2];
if (!castling.equals("-")) {
if (!castling.contains("K")) forceMoved(board, 7, 0);
if (!castling.contains("Q")) forceMoved(board, 0, 0);
if (!castling.contains("k")) forceMoved(board, 7, 7);
if (!castling.contains("q")) forceMoved(board, 0, 7);
} else {
forceMoved(board, 4, 0); // Force White King moved to kill all castling
forceMoved(board, 4, 7); // Force Black King moved to kill all castling
}
Position epTarget = null;
try { if (!parts[3].equals("-")) epTarget = Position.fromAlgebraic(parts[3]); }
catch (com.glassbox.exception.OutOfBoundsException ex) { throw new IllegalArgumentException("Invalid FEN en-passant square", ex); }
int halfMove = parts.length > 4 ? Integer.parseInt(parts[4]) : 0;
board.initHistory(turn, epTarget, halfMove);
return board;
}
private static void forceMoved(Board b, int f, int r) {
Piece p = b.getPieceAt(new Position(f, r));
if (p != null) p.setHasMoved(true);
}
private static PieceType charToType(char c) {
return switch (Character.toLowerCase(c)) {
case 'r' -> PieceType.ROOK; case 'n' -> PieceType.KNIGHT;
case 'b' -> PieceType.BISHOP; case 'q' -> PieceType.QUEEN;
case 'k' -> PieceType.KING; case 'p' -> PieceType.PAWN;
default -> throw new IllegalArgumentException("Invalid piece: " + c);
};
}
}