package com.glassbox.checkers.domain;
import com.glassbox.chess.domain.Color;
import com.glassbox.chess.domain.Position;
import com.glassbox.uml.meta.UmlRole;
import com.glassbox.uml.meta.UmlStereotype;
@UmlStereotype("Entity")
@UmlRole(UmlRole.ENGINE)
public class CheckersBoard {
private final CheckersPiece[][] grid = new CheckersPiece[8][8];
public CheckersBoard() {
setupInitialPosition();
}
public void setupInitialPosition() {
for (int r = 0; r < 3; r++) {
for (int f = 0; f < 8; f++) {
if ((f + r) % 2 == 1) grid[f][r] = new CheckersPiece(Color.BLACK, false);
}
}
for (int r = 5; r < 8; r++) {
for (int f = 0; f < 8; f++) {
if ((f + r) % 2 == 1) grid[f][r] = new CheckersPiece(Color.WHITE, false);

}
}
}
public CheckersPiece getPieceAt(Position p) {
return isValid(p) ? grid[p.getFile()][p.getRank()] : null;
}
public void setPieceAt(Position p, CheckersPiece piece) {
if (isValid(p)) grid[p.getFile()][p.getRank()] = piece;
}
public boolean isValid(Position p) {
return p.getFile() >= 0 && p.getFile() < 8 && p.getRank() >= 0 && p.getRank() < 8;
}
}