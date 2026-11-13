package com.glassbox.engine.command;
/**
* <p><b>Command Pattern:</b> Encapsulates a generic action that can be executed and undone.</p>
*/
public interface Command {
void execute();
void undo();
String getNotation();
}