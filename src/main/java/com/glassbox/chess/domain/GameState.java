package com.glassbox.chess.domain;

/** Mutable game-state snapshot copied before each reversible move. */
public final class GameState {
    public Color turn;
    public Position enPassantTarget;
    public int halfMoveClock;
    public GameState(Color turn, Position enPassantTarget, int halfMoveClock) {
        this.turn=turn; this.enPassantTarget=enPassantTarget; this.halfMoveClock=halfMoveClock;
    }
    public GameState copy() { return new GameState(turn,enPassantTarget,halfMoveClock); }
}
