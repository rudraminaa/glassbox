package com.glassbox.ui.util;
import com.glassbox.chess.domain.Color;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
public class ChessClock {
private int whiteTimeSeconds;
private int blackTimeSeconds;
private int incrementSeconds;
private boolean isUntimed;
private final StringProperty whiteTimeProp = new SimpleStringProperty();
private final StringProperty blackTimeProp = new SimpleStringProperty();
private Timeline timeline;
private Color currentTurn = Color.WHITE;
private Runnable onTimeUp;
public ChessClock(String timeControl, Runnable onTimeUp) {
this.onTimeUp = onTimeUp;
parseTimeControl(timeControl);
updateProperties();
timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
timeline.setCycleCount(Animation.INDEFINITE);
}
private void parseTimeControl(String tc) {
if ("Untimed".equals(tc)) {
isUntimed = true;
return;
}
isUntimed = false;

String[] parts = tc.split("\\+");
int minutes = Integer.parseInt(parts[0]);
incrementSeconds = Integer.parseInt(parts[1]);
whiteTimeSeconds = minutes * 60;
blackTimeSeconds = minutes * 60;
}
public void start() { if (!isUntimed) timeline.play(); }
public void stop() { if (!isUntimed) timeline.stop(); }
public void switchTurn(Color newTurn) {
if (isUntimed) return;
if (currentTurn == Color.WHITE) whiteTimeSeconds += incrementSeconds;
else blackTimeSeconds += incrementSeconds;
currentTurn = newTurn;
updateProperties();
}
private void tick() {
if (currentTurn == Color.WHITE) {
whiteTimeSeconds--;
if (whiteTimeSeconds <= 0) handleTimeUp();
} else {
blackTimeSeconds--;
if (blackTimeSeconds <= 0) handleTimeUp();
}
updateProperties();
}
private void handleTimeUp() {
timeline.stop();
if (onTimeUp != null) onTimeUp.run();
}
private void updateProperties() {
whiteTimeProp.set(isUntimed ? "∞" : formatTime(whiteTimeSeconds));
blackTimeProp.set(isUntimed ? "∞" : formatTime(blackTimeSeconds));
}
private String formatTime(int totalSeconds) {
int m = Math.max(0, totalSeconds / 60);
int s = Math.max(0, totalSeconds % 60);
return String.format("%02d:%02d", m, s);
}
public StringProperty whiteTimeProperty() { return whiteTimeProp; }
public StringProperty blackTimeProperty() { return blackTimeProp; }
// Getters/Setters for serialization
public int getWhiteTimeSeconds() { return whiteTimeSeconds; }
public int getBlackTimeSeconds() { return blackTimeSeconds; }
public void setTimes(int w, int b) { this.whiteTimeSeconds = w; this.blackTimeSeconds = b; updateProperties(); }
}