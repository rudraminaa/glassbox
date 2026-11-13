package com.glassbox.checkers.domain;
import com.glassbox.chess.domain.Color;
import com.glassbox.uml.meta.UmlRole;
import com.glassbox.uml.meta.UmlStereotype;
@UmlStereotype("Entity")
@UmlRole(UmlRole.PIECE)
public class CheckersPiece {
private final Color color;
private boolean isKing;
public CheckersPiece(Color color, boolean isKing) {
this.color = color;
this.isKing = isKing;
}
public Color getColor() { return color; }
public boolean isKing() { return isKing; }
public void makeKing() { this.isKing = true; }
public void setKing(boolean value) { this.isKing = value; }
}