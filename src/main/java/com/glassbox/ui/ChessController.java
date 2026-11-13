package com.glassbox.ui;

import com.glassbox.chess.ai.*;
import com.glassbox.chess.domain.*;
import com.glassbox.chess.game.ChessGame;
import com.glassbox.chess.piece.Piece;
import com.glassbox.chess.rule.CheckDetector;
import com.glassbox.chess.rule.MoveValidator;
import com.glassbox.chess.command.MoveCommand;
import com.glassbox.uml.event.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/** Coordinates user actions; rules remain in the chess domain and AI runs off the UI thread. */
public final class ChessController {
    private ChessGame game;
    private final ChessBoardView boardView;
    private final RightPanelView rightPanel;
    private final Label statusLine;
    private Position selected;
    private boolean flipped;
    private Move lastMove;

    private volatile boolean aiEnabled = true;
    private volatile String aiDifficulty = "Medium";
    private volatile boolean aiThinking;
    private Thread aiThread;
    private AtomicBoolean aiCancelled = new AtomicBoolean(false);

    public ChessController(ChessGame game, ChessBoardView board, RightPanelView panel, Label status) {
        this.game = game;
        boardView = board;
        rightPanel = panel;
        statusLine = status;
        board.setOnSquareClicked(this::click);
        refresh();
    }

    public void setAiEnabled(boolean enabled) {
        aiEnabled = enabled;
        if (!enabled) cancelAi();
        else scheduleAiIfNeeded();
        statusLine.setText(enabled ? "●  Computer opponent enabled · Black plays automatically"
                                   : "●  Two-player mode");
    }

    public void setAiDifficulty(String difficulty) {
        aiDifficulty = difficulty == null ? "Medium" : difficulty;
        statusLine.setText("●  AI difficulty · " + aiDifficulty);
    }

    private void click(Position p) {
        if (game.getCurrentPhase().isTerminal() || aiThinking ||
                (aiEnabled && game.getBoard().getGameState().turn == Color.BLACK)) return;
        Piece at = game.getBoard().getPieceAt(p);
        if (selected == null) {
            if (at != null && at.getColor() == game.getBoard().getGameState().turn) {
                selected = p;
                EventBus.getInstance().publish(new DomainEvents.PieceSelected(p));
                EventBus.getInstance().publish(new DomainEvents.RuntimeMessage("Player", "Player", "Board", "select("+p.toAlgebraic()+")", at.getClass().getSimpleName(), "Selected live piece at "+p.toAlgebraic()));
            } else return;
        } else {
            Piece selectedPiece = game.getBoard().getPieceAt(selected);
            String selectedType = selectedPiece == null ? "Piece" : selectedPiece.getClass().getSimpleName();
            EventBus.getInstance().publish(new DomainEvents.RuntimeMessage("Player", "ChessController", "ChessGame", "getLegalMovesFrom("+selected.toAlgebraic()+")", selectedType, "Request legal destinations"));
            EventBus.getInstance().publish(new DomainEvents.RuntimeMessage("Player", "ChessGame", "MoveValidator", "getLegalMoves(board, turn)", selectedType, "Generate and filter legal candidates"));
            List<Move> matches = game.getLegalMovesFrom(selected).stream()
                    .filter(m -> m.getTo().equals(p)).toList();
            EventBus.getInstance().publish(new DomainEvents.MoveAttempted(selected, p));
            if (matches.isEmpty()) {
                if (at != null && at.getColor() == game.getBoard().getGameState().turn) {
                    selected = p;
                    EventBus.getInstance().publish(new DomainEvents.PieceSelected(p));
                    EventBus.getInstance().publish(new DomainEvents.RuntimeMessage("Player", "Player", "Board", "select("+p.toAlgebraic()+")", at.getClass().getSimpleName(), "Changed selected live instance"));
                } else {
                    EventBus.getInstance().publish(new DomainEvents.MoveRejected(selected, p, "Destination is not a legal move"));
                    selected = null;
                }
            } else {
                Move chosen = chooseMove(matches);
                if (chosen == null) { selected = null; refresh(); return; }
                commit(chosen);
                selected = null;
            }
        }
        refresh();
    }

    private Move chooseMove(List<Move> matches) {
        if (matches.size() == 1) return matches.get(0);
        ChoiceDialog<PieceType> dialog = new ChoiceDialog<>(PieceType.QUEEN,
                PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT);
        dialog.setTitle("Pawn promotion");
        dialog.setHeaderText("Choose the piece for promotion");
        dialog.setContentText("Promote to:");
        Optional<PieceType> choice = dialog.showAndWait();
        return choice.flatMap(type -> matches.stream()
                .filter(m -> m instanceof PromotionMove pm && pm.getPromotionType() == type)
                .findFirst()).orElse(null);
    }

    private void commit(Move move) {
        try {
            Piece movingPiece = game.getBoard().getPieceAt(move.getFrom());
            String actor = aiEnabled && game.getBoard().getGameState().turn == Color.BLACK ? "AI Agent" : "Player";
            String pieceType = movingPiece == null ? "Piece" : movingPiece.getClass().getSimpleName();
            EventBus.getInstance().publish(new DomainEvents.RuntimeMessage(actor, actor, "ChessController", "commitMove()", pieceType, "Move "+move.getFrom().toAlgebraic()+" → "+move.getTo().toAlgebraic()));
            EventBus.getInstance().publish(new DomainEvents.RuntimeMessage(actor, "ChessController", "ChessGame", "createCommand() / makeMove()", pieceType, "Command pattern: validate and execute move"));
            MoveCommand command = game.createCommand(move);
            game.makeMove(command);
            EventBus.getInstance().publish(new DomainEvents.RuntimeMessage(actor, "ChessGame", "Board", "applyMove()", pieceType, "Board state mutated by domain command"));
            EventBus.getInstance().publish(new DomainEvents.RuntimeMessage(actor, "ChessGame", "EventBus", "publish(MoveCommitted)", pieceType, "Observers notified after committed move"));
            lastMove = move;
        } catch (Exception ex) {
            statusLine.setText("Move not played · " + ex.getMessage());
            EventBus.getInstance().publish(new DomainEvents.MoveRejected(move.getFrom(), move.getTo(), ex.getMessage()));
        }
    }

    public void newGame() {
        cancelAi();
        game = new ChessGame();
        selected = null;
        lastMove = null;
        refresh();
    }

    public void undo() {
        cancelAi();
        try {
            if (aiEnabled && game.getBoard().getGameState().turn == Color.WHITE &&
                    !game.getHistory().getNotationHistory().isEmpty()) {
                game.undo();
                if (!game.getHistory().getNotationHistory().isEmpty()) game.undo();
            } else game.undo();
            EventBus.getInstance().publish(new DomainEvents.RuntimeMessage("Player", "Player", "CommandHistory", "undo()", "", "Reverted the latest move command and restored the previous board snapshot"));
            EventBus.getInstance().publish(new DomainEvents.RuntimeMessage("Player", "CommandHistory", "ChessGame", "undoCommand()", "", "Restored prior game state"));
            selected = null; lastMove = null; refresh();
        } catch (Exception e) { statusLine.setText(e.getMessage()); }
    }

    public void redo() {
        cancelAi();
        try { game.redo(); EventBus.getInstance().publish(new DomainEvents.RuntimeMessage("Player", "Player", "CommandHistory", "redo()", "", "Re-applied next command")); selected = null; lastMove = null; refresh(); }
        catch (Exception e) { statusLine.setText(e.getMessage()); }
    }

    public void flip() { flipped = !flipped; refresh(); }

    private void refresh() {
        Board b = game.getBoard();
        List<Position> destinations = selected == null ? List.of() :
                game.getLegalMovesFrom(selected).stream().map(Move::getTo).distinct().collect(Collectors.toList());
        boolean check = CheckDetector.isInCheck(b, b.getGameState().turn);
        boardView.draw(b, selected, destinations, lastMove, check, flipped);
        rightPanel.update(b, b.getGameState().turn, game.getHistory().getNotationHistory(),
                material(b), game.getStatus(), lastMove, selected);
        String side = b.getGameState().turn == Color.WHITE ? "White" : "Black";
        String state = switch (game.getStatus()) {
            case ACTIVE -> side + " to move";
            case CHECK -> side + " is in check";
            case CHECKMATE -> "Checkmate";
            case STALEMATE -> "Stalemate";
            case DRAW -> "Draw";
        };
        statusLine.setText(aiThinking ? "●  AI thinking…  ·  " + game.getHistory().getNotationHistory().size() + " plies"
                : "●  " + state + (aiEnabled ? "  ·  vs computer (" + aiDifficulty + ")" : "  ·  two-player")
                + "     ·     " + game.getHistory().getNotationHistory().size() + " plies");
        scheduleAiIfNeeded();
    }

    private void scheduleAiIfNeeded() {
        if (!aiEnabled || aiThinking || game.getCurrentPhase().isTerminal() ||
                game.getBoard().getGameState().turn != Color.BLACK) return;
        aiThinking = true;
        aiCancelled = new AtomicBoolean(false);
        final AtomicBoolean cancellation = aiCancelled;
        final Board snapshot = game.getBoard().deepCopy();
        final String difficulty = aiDifficulty;
        statusLine.setText("●  AI thinking…");
        EventBus.getInstance().publish(new DomainEvents.RuntimeMessage("AI Agent", "AI Agent", "AIStrategy", "calculateMove(snapshot)", "", "AI search dispatched on background thread · difficulty = "+difficulty));
        aiThread = new Thread(() -> {
            Move calculated = null;
            try {
                AIStrategy strategy = switch (difficulty) {
                    case "Easy" -> new RandomAI();
                    case "Hard" -> new MinimaxAI(5, 1800);
                    default -> new MinimaxAI(3, 900);
                };
                calculated = strategy.calculateMove(snapshot, cancellation::get);
            } catch (Throwable ignored) {
                // A failed search is isolated from the JavaFX application thread.
            }
            final Move result = calculated;
            Platform.runLater(() -> {
                if (cancellation.get()) return;
                aiThinking = false;
                aiThread = null;
                if (result != null && aiEnabled && !game.getCurrentPhase().isTerminal() &&
                        game.getBoard().getGameState().turn == Color.BLACK) {
                    Move liveMove = MoveValidator.getLegalMoves(game.getBoard(), Color.BLACK).stream()
                            .filter(m -> m.getFrom().equals(result.getFrom()) && m.getTo().equals(result.getTo())
                                    && promotionMatches(m, result)).findFirst().orElse(null);
                    if (liveMove != null) commit(liveMove);
                }
                refresh();
            });
        }, "glassbox-ai-" + difficulty.toLowerCase(Locale.ROOT));
        aiThread.setDaemon(true);
        aiThread.start();
    }

    private boolean promotionMatches(Move live, Move calculated) {
        if (live instanceof PromotionMove a && calculated instanceof PromotionMove b)
            return a.getPromotionType() == b.getPromotionType();
        return !(live instanceof PromotionMove) && !(calculated instanceof PromotionMove);
    }

    private void cancelAi() {
        aiCancelled.set(true);
        if (aiThread != null) aiThread.interrupt();
        aiThread = null;
        aiThinking = false;
    }

    private int material(Board b) {
        int n = 0;
        for (int f = 0; f < 8; f++) for (int r = 0; r < 8; r++) {
            Piece p = b.getPieceAt(new Position(f, r));
            if (p != null) {
                int v = switch (p.getType()) {
                    case PAWN -> 1; case KNIGHT, BISHOP -> 3; case ROOK -> 5; case QUEEN -> 9; case KING -> 0;
                };
                n += p.getColor() == Color.WHITE ? v : -v;
            }
        }
        return n;
    }
}
