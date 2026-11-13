package com.glassbox.engine.state;
import com.glassbox.engine.core.Game;
/**
* <p><b>State Pattern:</b> Represents a distinct phase of the game. Handles inputs differently
* depending on the current state.</p>
*/
public interface GamePhase<M> {
void handleMove(Game<M> context, M move) throws Exception;
String getPhaseName();
boolean isTerminal();
}