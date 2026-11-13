package com.glassbox.chess.ai;
import com.glassbox.chess.domain.Board; import com.glassbox.chess.domain.Move; import java.util.function.Supplier;
public interface AIStrategy { Move calculateMove(Board board, Supplier<Boolean> isCancelled); }
