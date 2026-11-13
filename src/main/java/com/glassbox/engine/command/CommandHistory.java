package com.glassbox.engine.command;
import com.glassbox.exception.NoMoveToUndoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public class CommandHistory {
private final Stack<Command> undoStack = new Stack<>();
private final Stack<Command> redoStack = new Stack<>();
public void execute(Command command) {

command.execute();
undoStack.push(command);
redoStack.clear(); // A new timeline branch clears redos
}
public void undo() throws NoMoveToUndoException {
if (undoStack.isEmpty()) {
throw new NoMoveToUndoException("No moves available to undo.");
}
Command cmd = undoStack.pop();
cmd.undo();
redoStack.push(cmd);
}
public void redo() throws NoMoveToUndoException {
if (redoStack.isEmpty()) {
throw new NoMoveToUndoException("No moves available to redo.");
}
Command cmd = redoStack.pop();
cmd.execute();
undoStack.push(cmd);
}
public List<String> getNotationHistory() {
List<String> history = new ArrayList<>();
for (Command cmd : undoStack) history.add(cmd.getNotation());
return history;
}
}