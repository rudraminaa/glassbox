package com.glassbox.ui;

import com.glassbox.chess.domain.*;
import com.glassbox.chess.piece.Piece;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.util.List;
import java.util.function.Consumer;

/** Responsive board renderer. All rule decisions remain in the chess domain. */
public final class ChessBoardView {
    private final GridPane pane = new GridPane();
    private final Button[][] cells = new Button[8][8];
    private Consumer<Position> clickHandler;
    private static final String LIGHT = "#EEEED2", DARK = "#769656";

    public ChessBoardView() {
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(0); pane.setVgap(0);
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        for (int i = 0; i < 9; i++) {
            ColumnConstraints col = new ColumnConstraints(); col.setPercentWidth(i == 0 ? 4 : 12); col.setHgrow(Priority.ALWAYS); pane.getColumnConstraints().add(col);
            RowConstraints row = new RowConstraints(); row.setPercentHeight(i == 8 ? 4 : 12); row.setVgrow(Priority.ALWAYS); pane.getRowConstraints().add(row);
        }
        for (int y = 0; y < 8; y++) for (int x = 0; x < 8; x++) {
            final int file=x, rank=7-y;
            Button cell = new Button(); cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); cell.setMinSize(30,30);
            cell.setFocusTraversable(false);
            cell.setOnAction(e -> { if (clickHandler != null) clickHandler.accept(new Position(file,rank)); });
            cells[file][rank]=cell; pane.add(cell,x+1,y);
        }
        for (int x=0;x<8;x++) { Label l=new Label(String.valueOf((char)('a'+x))); l.getStyleClass().add("coordinate"); l.setMaxWidth(Double.MAX_VALUE); l.setAlignment(Pos.CENTER); pane.add(l,x+1,8); }
        for (int y=0;y<8;y++) { Label l=new Label(String.valueOf(8-y)); l.getStyleClass().add("coordinate"); l.setMaxWidth(Double.MAX_VALUE); l.setAlignment(Pos.CENTER); pane.add(l,0,y); }
    }
    public GridPane getPane(){return pane;}
    public void setOnSquareClicked(Consumer<Position> handler){clickHandler=handler;}

    public void draw(Board board, Position selected, List<Position> legal, Move last, boolean check, boolean flipped) {
        for(int y=0;y<8;y++) for(int x=0;x<8;x++) {
            int f=flipped?7-x:x, r=flipped?y:7-y; Position p=new Position(f,r);
            Piece piece=board.getPieceAt(p); Button b=cells[f][r];
            b.setText(piece==null?"":symbol(piece));
            String bg=((f+r)%2==0)?LIGHT:DARK;
            if(last!=null&&(p.equals(last.getFrom())||p.equals(last.getTo()))) bg="#C8C96A";
            if(legal!=null&&legal.contains(p)) bg=piece==null?"#A9C985":"#D1A15D";
            if(p.equals(selected)) bg="#E7C65B";
            if(check&&piece!=null&&piece.getType()==PieceType.KING&&piece.getColor()==board.getGameState().turn) bg="#D86B6B";
            String fg="#182016";
            b.setStyle("-fx-background-color:"+bg+"; -fx-background-radius:0; -fx-border-color:rgba(20,25,15,.12); -fx-border-width:0.5; -fx-font-size:34px; -fx-text-fill:"+fg+"; -fx-font-family:'Segoe UI Symbol','DejaVu Sans'; -fx-padding:0;");
        }
    }
    private String symbol(Piece p){return switch(p.getType()){case KING->p.getColor()==Color.WHITE?"♔":"♚";case QUEEN->p.getColor()==Color.WHITE?"♕":"♛";case ROOK->p.getColor()==Color.WHITE?"♖":"♜";case BISHOP->p.getColor()==Color.WHITE?"♗":"♝";case KNIGHT->p.getColor()==Color.WHITE?"♘":"♞";case PAWN->p.getColor()==Color.WHITE?"♙":"♟";};}
}
