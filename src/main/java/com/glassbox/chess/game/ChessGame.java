package com.glassbox.chess.game;
import com.glassbox.chess.command.MoveCommand; import com.glassbox.chess.domain.*; import com.glassbox.chess.rule.GameStateEvaluator; import com.glassbox.chess.rule.MoveValidator; import com.glassbox.chess.state.*; import com.glassbox.chess.util.SanConverter; import com.glassbox.engine.command.CommandHistory; import com.glassbox.engine.core.Game; import com.glassbox.engine.state.GamePhase; import com.glassbox.exception.GameOverException; import com.glassbox.uml.event.*; import java.util.*; import java.util.stream.Collectors;
/** Facade for a chess match; UI code calls this rather than mutating the board directly. */
public class ChessGame implements Game<MoveCommand>{
 private final Board board=new Board(); private final CommandHistory history=new CommandHistory(); private GamePhase<MoveCommand> phase=new InProgressPhase(); private GameStatus status=GameStatus.ACTIVE;
 public ChessGame(){board.setupInitialPosition();EventBus.getInstance().publish(new DomainEvents.GameStarted());}
 public void makeMove(MoveCommand cmd)throws Exception{if(phase.isTerminal())throw new GameOverException("Game has ended");history.execute(cmd);evaluateStateTransition();EventBus.getInstance().publish(new DomainEvents.MoveCommitted(cmd.getMove()));}
 public void undo()throws Exception{history.undo();evaluateStateTransition();EventBus.getInstance().publish(new DomainEvents.HistoryChanged("UNDO"));}
 public void redo()throws Exception{history.redo();evaluateStateTransition();EventBus.getInstance().publish(new DomainEvents.HistoryChanged("REDO"));}
 public void resign(){status=GameStatus.CHECKMATE;phase=new GameOverPhase();EventBus.getInstance().publish(new DomainEvents.GameEnded(status));}
 public void offerDraw(){status=GameStatus.DRAW;phase=new GameOverPhase();EventBus.getInstance().publish(new DomainEvents.GameEnded(status));}
 public void evaluateStateTransition(){status=GameStateEvaluator.evaluate(board);phase=switch(status){case ACTIVE->new InProgressPhase();case CHECK->new CheckPhase();case CHECKMATE,STALEMATE,DRAW->new GameOverPhase();};EventBus.getInstance().publish(new DomainEvents.StatusChanged(status));}
 public MoveCommand createCommand(Move move){return new MoveCommand(board,move,SanConverter.toSan(board,move));}
 public List<Move> getLegalMovesFrom(Position p){return MoveValidator.getLegalMoves(board,board.getGameState().turn).stream().filter(m->m.getFrom().equals(p)).collect(Collectors.toList());}
 public GamePhase<MoveCommand> getCurrentPhase(){return phase;} public CommandHistory getHistory(){return history;} public Board getBoard(){return board;} public GameStatus getStatus(){return status;}
}
