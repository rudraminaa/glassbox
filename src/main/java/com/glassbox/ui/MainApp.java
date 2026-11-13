package com.glassbox.ui;

import com.glassbox.chess.ai.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/** GlassBox desktop shell. Press U to toggle the inspector. */
public final class MainApp extends Application {
    @Override public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");

        ChessBoardView board = new ChessBoardView();
        RightPanelView panel = new RightPanelView();
        Label status = new Label("●  READY");
        status.getStyleClass().add("status-line");
        ChessController controller = new ChessController(
                new com.glassbox.chess.game.ChessGame(), board, panel, status);

        HBox toolbar = new HBox(8);
        toolbar.getStyleClass().add("toolbar");
        toolbar.setPadding(new Insets(10, 16, 10, 16));
        Label brand = new Label("GLASSBOX");
        brand.getStyleClass().add("brand");
        Label divider = new Label("/  CHESS");
        divider.getStyleClass().add("brand-sub");
        Button fresh = toolButton("New game");
        Button undo = toolButton("Undo");
        Button redo = toolButton("Redo");
        Button flip = toolButton("Flip board");
        ToggleButton aiToggle = new ToggleButton("Play vs AI");
        aiToggle.getStyleClass().add("tool-button");
        aiToggle.setSelected(true);
        ComboBox<String> difficulty = new ComboBox<>();
        difficulty.getItems().addAll("Easy", "Medium", "Hard");
        difficulty.setValue("Medium");
        difficulty.getStyleClass().add("difficulty-picker");
        ToggleButton inspectorToggle = new ToggleButton("Hide inspector");
        inspectorToggle.getStyleClass().add("tool-button");
        inspectorToggle.setSelected(true);

        fresh.setOnAction(e -> controller.newGame());
        undo.setOnAction(e -> controller.undo());
        redo.setOnAction(e -> controller.redo());
        flip.setOnAction(e -> controller.flip());
        aiToggle.selectedProperty().addListener((o, old, selected) -> controller.setAiEnabled(selected));
        difficulty.setOnAction(e -> controller.setAiDifficulty(difficulty.getValue()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label shortcut = new Label("U  ·  TOGGLE INSPECTOR");
        shortcut.getStyleClass().add("shortcut-hint");
        toolbar.getChildren().addAll(brand, divider, new Separator(javafx.geometry.Orientation.VERTICAL),
                fresh, undo, redo, flip, new Separator(javafx.geometry.Orientation.VERTICAL),
                aiToggle, difficulty, spacer, shortcut, inspectorToggle);

        StackPane boardFrame = new StackPane(board.getPane());
        boardFrame.getStyleClass().add("board-frame");
        boardFrame.setPadding(new Insets(12));
        SplitPane workspace = new SplitPane(boardFrame, panel.getPane());
        workspace.getStyleClass().add("workspace-split");
        workspace.setDividerPositions(0.76);
        workspace.setPadding(new Insets(12, 16, 12, 16));
        SplitPane.setResizableWithParent(boardFrame, true);
        SplitPane.setResizableWithParent(panel.getPane(), true);
        inspectorToggle.setOnAction(e -> toggleInspector(inspectorToggle, panel, workspace));

        root.setTop(toolbar);
        root.setCenter(workspace);
        root.setBottom(status);
        Scene scene = new Scene(root, 1440, 920);
        scene.getStylesheets().add(getClass().getResource("/glassbox.css").toExternalForm());
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.U) {
                inspectorToggle.setSelected(!inspectorToggle.isSelected());
                toggleInspector(inspectorToggle, panel, workspace);
            }
        });
        stage.setTitle("GlassBox Engine — Chess");
        stage.setMinWidth(1050);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();
        workspace.setDividerPositions(0.76);
    }

    private static Button toolButton(String text) {
        Button b = new Button(text);
        b.getStyleClass().add("tool-button");
        return b;
    }

    private static void toggleInspector(ToggleButton button, RightPanelView panel, SplitPane workspace) {
        boolean show = button.isSelected();
        if (show && !workspace.getItems().contains(panel.getPane())) {
            workspace.getItems().add(panel.getPane());
            workspace.setDividerPositions(0.76);
        } else if (!show && workspace.getItems().contains(panel.getPane())) {
            workspace.getItems().remove(panel.getPane());
        }
        button.setText(show ? "Hide inspector" : "Show inspector");
    }

    public static void main(String[] args) { launch(args); }
}
