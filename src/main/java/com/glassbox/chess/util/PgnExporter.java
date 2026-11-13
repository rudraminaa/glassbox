package com.glassbox.chess.util;
import com.glassbox.chess.domain.GameStatus;
import java.util.List;
public class PgnExporter {
public static String export(List<String> sanHistory, GameStatus status) {
StringBuilder sb = new StringBuilder();
sb.append("[Event \"GlassBox Engine Game\"]\n");
sb.append("[Site \"Offline\"]\n");
sb.append("[Date \"").append(java.time.LocalDate.now()).append("\"]\n");
sb.append("[Round \"1\"]\n");
sb.append("[White \"Player\"]\n");
sb.append("[Black \"Opponent\"]\n");
String result = "*";
if (status == GameStatus.CHECKMATE) {
result = (sanHistory.size() % 2 != 0) ? "1-0" : "0-1"; // Rough deduction if turn-based
} else if (status == GameStatus.DRAW || status == GameStatus.STALEMATE) {
result = "1/2-1/2";
}
sb.append("[Result \"").append(result).append("\"]\n\n");
for (int i = 0; i < sanHistory.size(); i++) {
if (i % 2 == 0) {
sb.append((i / 2) + 1).append(". ");
}
sb.append(sanHistory.get(i)).append(" ");
}
sb.append(result);
return sb.toString().trim();
}
}