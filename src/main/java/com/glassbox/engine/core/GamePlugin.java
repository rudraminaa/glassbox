package com.glassbox.engine.core; import com.glassbox.engine.command.Command; import java.util.List;
/** Game-agnostic plugin boundary. */
public interface GamePlugin<B,M,S>{String getGameName();B createInitialBoard();List<M> getLegalMoves(B board,S turn);void applyMove(B board,M move);void undoMove(B board,M move);S getNextTurn(S currentTurn);boolean isGameOver(B board,S turn);String getStatusString(B board,S turn);Command createCommand(B board,M move);}
