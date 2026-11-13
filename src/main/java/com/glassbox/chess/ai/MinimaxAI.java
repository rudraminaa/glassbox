package com.glassbox.chess.ai;
import com.glassbox.chess.domain.*; import com.glassbox.chess.rule.MoveValidator; import java.util.*; import java.util.function.Supplier;
public class MinimaxAI implements AIStrategy {
 private final BoardEvaluator evaluator=new StandardEvaluator(); private final int targetDepth; private final long timeBudgetMs; private long start; private Supplier<Boolean> cancelled=()->false; private long nodes;
 public MinimaxAI(int depth,long budgetMs){targetDepth=Math.max(1,depth);timeBudgetMs=Math.max(1,budgetMs);}
 public long getNodesEvaluated(){return nodes;}
 @Override public Move calculateMove(Board board,Supplier<Boolean> cancel){cancelled=cancel==null?()->false:cancel;start=System.currentTimeMillis();nodes=0; Move best=null; Color side=board.getGameState().turn;
  for(int d=1;d<=targetDepth&&!stop();d++){int bestScore=Integer.MIN_VALUE; Move local=null; for(Move m:ordered(board,side)){if(stop())break;board.applyMove(m);int score=-search(board,d-1,-1_000_000,1_000_000,side.opposite());board.undoMove(m);if(score>bestScore){bestScore=score;local=m;}} if(!stop()&&local!=null)best=local;}
  if(best==null){List<Move> legal=MoveValidator.getLegalMoves(board,side);if(!legal.isEmpty())best=legal.get(0);} return best;
 }
 private int search(Board b,int depth,int alpha,int beta,Color side){nodes++;if(stop())return 0;if(depth==0)return evaluator.evaluate(b,side);List<Move> moves=ordered(b,side);if(moves.isEmpty())return evaluator.evaluate(b,side);int best=-1_000_000;for(Move m:moves){if(stop())break;b.applyMove(m);int score=-search(b,depth-1,-beta,-alpha,side.opposite());b.undoMove(m);best=Math.max(best,score);alpha=Math.max(alpha,score);if(alpha>=beta)break;}return best;}
 private List<Move> ordered(Board b,Color c){List<Move> m=new ArrayList<>(MoveValidator.getLegalMoves(b,c));m.sort(Comparator.comparingInt((Move x)->x.getCapturedPiece()==null?0:1).reversed());return m;}
 private boolean stop(){return cancelled.get()||System.currentTimeMillis()-start>=timeBudgetMs;}
}
