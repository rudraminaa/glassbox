package com.glassbox.checkers;
import com.glassbox.checkers.domain.*; import com.glassbox.checkers.rule.CheckersRules; import com.glassbox.chess.domain.Color; import com.glassbox.engine.command.Command; import com.glassbox.engine.core.GamePlugin; import java.util.List;
public class CheckersPlugin implements GamePlugin<CheckersBoard,CheckersMove,Color>{
 public String getGameName(){return "Checkers";} public CheckersBoard createInitialBoard(){return new CheckersBoard();}
 public List<CheckersMove> getLegalMoves(CheckersBoard b,Color c){return CheckersRules.getLegalMoves(b,c);} public void applyMove(CheckersBoard b,CheckersMove m){CheckersRules.applyMove(b,m);} public void undoMove(CheckersBoard b,CheckersMove m){CheckersRules.undoMove(b,m);} public Color getNextTurn(Color c){return c.opposite();}
 public boolean isGameOver(CheckersBoard b,Color c){return getLegalMoves(b,c).isEmpty();} public String getStatusString(CheckersBoard b,Color c){return isGameOver(b,c)?"GAME OVER":"ACTIVE";}
 public Command createCommand(CheckersBoard b,CheckersMove m){return new Command(){public void execute(){applyMove(b,m);}public void undo(){undoMove(b,m);}public String getNotation(){return m.getFrom()+"-"+m.getTo();}};}
}
