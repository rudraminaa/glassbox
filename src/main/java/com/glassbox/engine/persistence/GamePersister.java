package com.glassbox.engine.persistence;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.List;
public class GamePersister {
private static final String AUTOSAVE_FILE = "autosave.json";
private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
public static class SaveData {
public List<String> sanHistory;
public int whiteTimeSeconds;
public int blackTimeSeconds;
public String timeControl;
public String difficulty;
}

public static void saveGame(SaveData data, File file) throws IOException {
try (Writer writer = new FileWriter(file)) {
GSON.toJson(data, writer);
}
}
public static SaveData loadGame(File file) throws IOException {
try (Reader reader = new FileReader(file)) {
return GSON.fromJson(reader, SaveData.class);
}
}
public static void autosave(SaveData data) {
try { saveGame(data, new File(AUTOSAVE_FILE)); } catch (IOException ignored) {}
}
public static SaveData loadAutosave() {
File file = new File(AUTOSAVE_FILE);
if (file.exists()) {
try { return loadGame(file); } catch (IOException ignored) {}
}
return null;
}
}