package com.glassbox.uml.event; import com.glassbox.chess.domain.*;
public final class DomainEvents{private DomainEvents(){}public abstract static class BaseEvent implements GlassBoxEvent{private final long timestamp=System.currentTimeMillis();public long getTimestamp(){return timestamp;}}
public static class PieceSelected extends BaseEvent{public final Position position;public PieceSelected(Position p){position=p;}} public static class SelectionCleared extends BaseEvent{}
public static class MoveAttempted extends BaseEvent{public final Position from,to;public MoveAttempted(Position f,Position t){from=f;to=t;}}
public static class MoveCommitted extends BaseEvent{public final Move move;public MoveCommitted(Move m){move=m;}}
public static class MoveRejected extends BaseEvent{public final Position from,to;public final String reason;public MoveRejected(Position f,Position t,String r){from=f;to=t;reason=r;}}
public static class MoveUndone extends BaseEvent{public final Move move;public MoveUndone(Move m){move=m;}} public static class MoveRedone extends BaseEvent{public final Move move;public MoveRedone(Move m){move=m;}}
public static class StatusChanged extends BaseEvent{public final GameStatus status;public StatusChanged(GameStatus s){status=s;}} public static class GameStarted extends BaseEvent{} public static class GameEnded extends BaseEvent{public final GameStatus status;public GameEnded(GameStatus s){status=s;}} public static class PromotionChosen extends BaseEvent{public final PieceType type;public PromotionChosen(PieceType t){type=t;}} public static class HistoryChanged extends BaseEvent{public final String action;public HistoryChanged(String a){action=a;}}
public static class RuntimeMessage extends BaseEvent {
 public final String actor, from, to, method, pieceType, detail;
 public RuntimeMessage(String actor,String from,String to,String method,String pieceType,String detail){this.actor=actor;this.from=from;this.to=to;this.method=method;this.pieceType=pieceType;this.detail=detail;}
}
}
