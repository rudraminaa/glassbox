package com.glassbox.chess.domain;
import com.glassbox.exception.OutOfBoundsException;
import java.util.Objects;
/**
* Immutable value object representing a board coordinate.
* <p><b>SOLID (SRP):</b> Solely handles coordinate representation and conversion.</p>
*/
public final class Position {
private final int file; // 0-7 (a-h)
private final int rank; // 0-7 (1-8)
public Position(int file, int rank) {

this.file = file;
this.rank = rank;
}
public int getFile() { return file; }
public int getRank() { return rank; }
public String toAlgebraic() {
return "" + (char) ('a' + file) + (rank + 1);
}
public static Position fromAlgebraic(String alg) throws OutOfBoundsException {
if (alg == null || alg.length() != 2) throw new OutOfBoundsException("Invalid algebraic: " + alg);
int f = alg.charAt(0) - 'a';
int r = alg.charAt(1) - '1';
if (f < 0 || f > 7 || r < 0 || r > 7) throw new OutOfBoundsException("Algebraic out of bounds: " + alg);
return new Position(f, r);
}
@Override
public boolean equals(Object o) {
if (this == o) return true;
if (o == null || getClass() != o.getClass()) return false;
Position position = (Position) o;
return file == position.file && rank == position.rank;
}
@Override
public int hashCode() {
return Objects.hash(file, rank);
}
@Override public String toString() { return toAlgebraic(); }
}
