package com.glassbox.ui.util;
import com.glassbox.ui.config.Settings;
import javafx.scene.media.AudioClip;
import java.net.URL;
public class SoundManager {
private static AudioClip moveSound;
private static AudioClip captureSound;
static {
try {
URL moveUrl = SoundManager.class.getResource("/sounds/move.wav");
URL capUrl = SoundManager.class.getResource("/sounds/capture.wav");
if (moveUrl != null) moveSound = new AudioClip(moveUrl.toString());
if (capUrl != null) captureSound = new AudioClip(capUrl.toString());
} catch (Exception e) {
System.err.println("Audio resources not found, sounds disabled.");
}
}
public static void playMove() {
if (Settings.SOUND_ENABLED && moveSound != null) moveSound.play();
}
public static void playCapture() {
if (Settings.SOUND_ENABLED && captureSound != null) captureSound.play();
}
}