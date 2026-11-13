package com.glassbox.chess.domain;

import com.glassbox.chess.piece.*;
import java.util.*;

/** Board owns its 64 squares and maintains reversible chess state. */
public class Board {
    private final Square[][] grid = new Square[8][8];
    private GameState currentState = new GameState(Color.WHITE,null,0);
    private final Deque<GameState> stateHistory = new ArrayDeque<>();
    private final Map<String,Integer> repetitionMap = new HashMap<>();

    public Board() { for(int f=0;f<8;f++) for(int r=0;r<8;r++) grid[f][r]=new Square(new Position(f,r)); initHistory(Color.WHITE,null,0); }
    public boolean isValid(Position p) { return p!=null && p.getFile()>=0 && p.getFile()<8 && p.getRank()>=0 && p.getRank()<8; }
    public Piece getPieceAt(Position p) { return isValid(p)?grid[p.getFile()][p.getRank()].getPiece():null; }
    public void placePiece(Piece piece, Position p) { if(!isValid(p)) throw new IllegalArgumentException("Out of bounds: "+p); grid[p.getFile()][p.getRank()].setPiece(piece); if(piece!=null) piece.setPosition(p); }
    public void setPieceAt(Position p, Piece piece) { placePiece(piece,p); }
    public Piece removePiece(Position p) { if(!isValid(p)) return null; Piece x=getPieceAt(p); grid[p.getFile()][p.getRank()].setPiece(null); return x; }
    public GameState getGameState() { return currentState; }
    public void initHistory(Color turn, Position ep, int halfMove) { currentState=new GameState(turn,ep,halfMove); stateHistory.clear(); repetitionMap.clear(); repetitionMap.put(getPositionKey(),1); }
    public int getRepetitionCount(String key) { return repetitionMap.getOrDefault(key,0); }
    public void applyMove(Move move) {
        if(move==null) throw new IllegalArgumentException("Move cannot be null");
        stateHistory.push(currentState.copy());
        move.execute(this);
        GameState next=new GameState(currentState.turn.opposite(),null,currentState.halfMoveClock+1);
        if(move.getMovedPiece().getType()==PieceType.PAWN || move.getCapturedPiece()!=null) next.halfMoveClock=0;
        if(move.getMovedPiece().getType()==PieceType.PAWN && Math.abs(move.getTo().getRank()-move.getFrom().getRank())==2)
            next.enPassantTarget=new Position(move.getFrom().getFile(),(move.getFrom().getRank()+move.getTo().getRank())/2);
        currentState=next;
        String key=getPositionKey(); repetitionMap.merge(key,1,Integer::sum);
    }
    public void undoMove(Move move) {
        if(stateHistory.isEmpty()) throw new IllegalStateException("No move to undo");
        String key=getPositionKey(); int count=repetitionMap.getOrDefault(key,0); if(count<=1) repetitionMap.remove(key); else repetitionMap.put(key,count-1);
        move.undo(this); currentState=stateHistory.pop();
    }
    public String getPositionKey() {
        StringBuilder s=new StringBuilder();
        for(int r=7;r>=0;r--) for(int f=0;f<8;f++){ Piece p=getPieceAt(new Position(f,r)); if(p==null)s.append('.'); else {char c=switch(p.getType()){case KING->'k';case QUEEN->'q';case ROOK->'r';case BISHOP->'b';case KNIGHT->'n';case PAWN->'p';}; s.append(p.getColor()==Color.WHITE?Character.toUpperCase(c):c);} }
        s.append(currentState.turn==Color.WHITE?'w':'b').append('|');
        for(Color c:Color.values()){int rank=c==Color.WHITE?0:7; Piece k=getPieceAt(new Position(4,rank)); if(k!=null&&!k.hasMoved()&&k.getColor()==c){Piece kr=getPieceAt(new Position(7,rank)), qr=getPieceAt(new Position(0,rank)); if(kr!=null&&!kr.hasMoved()&&kr.getType()==PieceType.ROOK)s.append(c==Color.WHITE?'K':'k'); if(qr!=null&&!qr.hasMoved()&&qr.getType()==PieceType.ROOK)s.append(c==Color.WHITE?'Q':'q');}}
        s.append('|').append(currentState.enPassantTarget==null?"-":currentState.enPassantTarget.toAlgebraic()); return s.toString();
    }
    public boolean isInsufficientMaterial() {
        List<Piece> minors=new ArrayList<>();
        for(int f=0;f<8;f++)for(int r=0;r<8;r++){Piece p=getPieceAt(new Position(f,r)); if(p==null||p.getType()==PieceType.KING)continue; if(p.getType()==PieceType.PAWN||p.getType()==PieceType.ROOK||p.getType()==PieceType.QUEEN)return false; minors.add(p);}
        if(minors.size()<=1)return true;
        if(minors.stream().allMatch(p->p.getType()==PieceType.BISHOP)){ int color=-1; for(Piece p:minors){int sq=(p.getPosition().getFile()+p.getPosition().getRank())%2;if(color<0)color=sq;else if(color!=sq)return false;} return true; }
        return false;
    }
    public void setupInitialPosition() {
        for(int f=0;f<8;f++)for(int r=0;r<8;r++)grid[f][r].setPiece(null);
        PieceType[] back={PieceType.ROOK,PieceType.KNIGHT,PieceType.BISHOP,PieceType.QUEEN,PieceType.KING,PieceType.BISHOP,PieceType.KNIGHT,PieceType.ROOK};
        for(int f=0;f<8;f++){placePiece(PieceFactory.createPiece(PieceType.PAWN,Color.WHITE,new Position(f,1)),new Position(f,1));placePiece(PieceFactory.createPiece(PieceType.PAWN,Color.BLACK,new Position(f,6)),new Position(f,6));placePiece(PieceFactory.createPiece(back[f],Color.WHITE,new Position(f,0)),new Position(f,0));placePiece(PieceFactory.createPiece(back[f],Color.BLACK,new Position(f,7)),new Position(f,7));}
        initHistory(Color.WHITE,null,0);
    }
    public Board deepCopy() {
        Board b=new Board();
        for(int f=0;f<8;f++)for(int r=0;r<8;r++){Piece p=getPieceAt(new Position(f,r));if(p!=null){Piece q=PieceFactory.createPiece(p.getType(),p.getColor(),new Position(f,r));q.setHasMoved(p.hasMoved());b.placePiece(q,new Position(f,r));}}
        b.initHistory(currentState.turn,currentState.enPassantTarget,currentState.halfMoveClock); b.repetitionMap.clear(); b.repetitionMap.putAll(repetitionMap); return b;
    }
}
