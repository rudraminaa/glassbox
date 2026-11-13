package com.glassbox.chess.command;
import com.glassbox.chess.domain.Board;
import com.glassbox.chess.domain.Move;
import com.glassbox.engine.command.Command;
/**
* Adapts the domain `Move` into the engine's generic `Command` structure.
*/
public class MoveCommand implements Command {
private final Board board;
private final Move move;
private final String sanNotation;
public MoveCommand(Board board, Move move, String sanNotation) {
this.board = board;
this.move = move;
this.sanNotation = sanNotation;
}
@Override
public void execute() { board.applyMove(move); }
@Override
public void undo() { board.undoMove(move); }
@Override
public String getNotation() { return sanNotation; }
public Move getMove() { return move; }
}