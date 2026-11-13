package com.glassbox.chess.util;
import com.glassbox.chess.domain.Board;
import com.glassbox.chess.domain.Color;
import com.glassbox.chess.domain.Position;
import com.glassbox.chess.piece.Piece;
public class AsciiBoardPrinter {

public static String print(Board board) {
StringBuilder sb = new StringBuilder();
for (int rank = 7; rank >= 0; rank--) {
sb.append(rank + 1).append(" ");
for (int file = 0; file < 8; file++) {
Piece p = board.getPieceAt(new Position(file, rank));
if (p == null) {
sb.append(". ");
} else {
char c = switch (p.getType()) {
case KING -> 'K'; case QUEEN -> 'Q'; case ROOK -> 'R';
case BISHOP -> 'B'; case KNIGHT -> 'N'; case PAWN -> 'P';
};
sb.append(p.getColor() == Color.BLACK ? Character.toLowerCase(c) : c).append(" ");
}
}
sb.append("\n");
}
sb.append(" a b c d e f g h\n");
return sb.toString();
}
}