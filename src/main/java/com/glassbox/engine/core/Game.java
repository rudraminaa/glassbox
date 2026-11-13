package com.glassbox.engine.core;
import com.glassbox.engine.state.GamePhase;
/**
* <p><b>Facade Pattern:</b> Provides a unified, high-level interface to the complex subsystems
* of rule evaluation, board state, and history.</p>
*/
public interface Game<M> {
void makeMove(M move) throws Exception;
void undo() throws Exception;
void redo() throws Exception;
void resign();
void offerDraw();
GamePhase<M> getCurrentPhase();
}