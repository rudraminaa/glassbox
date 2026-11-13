package com.glassbox.chess.rule;
import com.glassbox.chess.domain.*;
import com.glassbox.chess.piece.Piece;

/** Attack detection deliberately differs from legal-move generation (notably for pawns). */
public final class CheckDetector {
 private CheckDetector() {}
 public static boolean isInCheck(Board board, Color color) { Position king=findKing(board,color); return king!=null && isSquareAttacked(board,king,color.opposite()); }
 public static boolean isSquareAttacked(Board b, Position target, Color by) {
  for(int f=0;f<8;f++) for(int r=0;r<8;r++){ Piece p=b.getPieceAt(new Position(f,r)); if(p==null||p.getColor()!=by)continue;
   int df=target.getFile()-f, dr=target.getRank()-r;
   switch(p.getType()){
    case PAWN -> {int dir=by==Color.WHITE?1:-1;if(Math.abs(df)==1&&dr==dir)return true;}
    case KNIGHT -> {if((Math.abs(df)==1&&Math.abs(dr)==2)||(Math.abs(df)==2&&Math.abs(dr)==1))return true;}
    case KING -> {if(Math.max(Math.abs(df),Math.abs(dr))==1)return true;}
    case ROOK, BISHOP, QUEEN -> {boolean straight=(df==0||dr==0), diagonal=Math.abs(df)==Math.abs(dr); if((p.getType()==PieceType.ROOK&&!straight)||(p.getType()==PieceType.BISHOP&&!diagonal)||(p.getType()==PieceType.QUEEN&&!(straight||diagonal))|| (df==0&&dr==0))continue; int sf=Integer.signum(df),sr=Integer.signum(dr),x=f+sf,y=r+sr; boolean clear=true; while(x!=target.getFile()||y!=target.getRank()){if(b.getPieceAt(new Position(x,y))!=null){clear=false;break;}x+=sf;y+=sr;} if(clear)return true;}
   }
  } return false;
 }
 public static Position findKing(Board b, Color c){for(int f=0;f<8;f++)for(int r=0;r<8;r++){Piece p=b.getPieceAt(new Position(f,r));if(p!=null&&p.getColor()==c&&p.getType()==PieceType.KING)return p.getPosition();}return null;}
}
