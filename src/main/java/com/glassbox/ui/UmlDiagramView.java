package com.glassbox.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Responsive PlantUML viewport.
 *
 * At 100% the diagram is fitted to the available panel width. Taller diagrams
 * scroll vertically; zooming above 100% enables horizontal panning as needed.
 */
final class UmlDiagramView extends BorderPane {
    private final TextArea source = new TextArea();
    private final StackPane visual = new StackPane();
    private final ScrollPane scroll = new ScrollPane();
    private final ImageView image = new ImageView();
    private final Label status = new Label("Waiting for runtime model");
    private final Slider zoom = new Slider(.65, 1.8, 1.0);
    private final Button sourceToggle = new Button("Source");
    private String currentSource = "";
    private boolean showingSource;

    UmlDiagramView() {
        getStyleClass().add("uml-view");
        setMinSize(0, 0);
        setStyle("-fx-background-color:#111827;");

        source.setEditable(false);
        source.setWrapText(false);
        source.setStyle("-fx-control-inner-background:#111827;-fx-text-fill:#dbe4ef;-fx-font-family:monospace;");
        source.setMinSize(0, 0);

        image.setPreserveRatio(true);
        image.setSmooth(true);
        image.setFitWidth(700);
        image.setFitHeight(0);
        image.setMouseTransparent(true);

        scroll.setContent(image);
        scroll.setPannable(true);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setMinSize(0, 0);
        scroll.setStyle("-fx-background:#111827;-fx-background-color:#111827;");
        scroll.getStyleClass().add("diagram-scroll");

        // Recompute the rendered image width whenever the inspector is resized.
        scroll.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> resizeDiagram());
        zoom.valueProperty().addListener((obs, oldValue, newValue) -> resizeDiagram());

        sourceToggle.setOnAction(e -> {
            showingSource = !showingSource;
            visual.getChildren().setAll(showingSource ? source : scroll);
            sourceToggle.setText(showingSource ? "Diagram" : "Source");
        });

        Button fit = new Button("Fit");
        fit.setOnAction(e -> zoom.setValue(1.0));
        Button minus = new Button("−");
        Button plus = new Button("+");
        minus.setOnAction(e -> zoom.setValue(Math.max(zoom.getMin(), zoom.getValue() - .1)));
        plus.setOnAction(e -> zoom.setValue(Math.min(zoom.getMax(), zoom.getValue() + .1)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label zoomLabel = new Label("ZOOM");
        HBox toolbar = new HBox(7, sourceToggle, spacer, zoomLabel, minus, zoom, plus, fit);
        toolbar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(7, 9, 7, 9));
        toolbar.getStyleClass().add("uml-toolbar");
        zoom.setPrefWidth(105);
        zoom.setMinWidth(72);

        status.getStyleClass().add("diagram-status");
        status.setPadding(new Insets(4, 10, 6, 10));
        visual.getChildren().add(scroll);
        setTop(toolbar);
        setCenter(visual);
        setBottom(status);
        visual.setMinSize(0, 0);
    }

    private void resizeDiagram() {
        if (image.getImage() == null) return;
        double viewportWidth = scroll.getViewportBounds().getWidth();
        if (viewportWidth <= 1) return;
        // Leave a small gutter at 100%; zoom > 100% deliberately overflows
        // and uses the ScrollPane's horizontal scrollbar/panning.
        double fittedWidth = Math.max(1, viewportWidth - 18);
        image.setFitWidth(fittedWidth * zoom.getValue());
    }

    void setSource(String uml) {
        if (uml == null || uml.isBlank() || uml.equals(currentSource)) return;
        currentSource = uml;
        source.setText(uml);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            SourceStringReader reader = new SourceStringReader(uml);
            var description = reader.outputImage(out);
            if (out.size() == 0) throw new IllegalStateException("PlantUML produced no image");
            String renderDescription = description == null ? "" : description.getDescription();
            String diagnostic = "";
            if (!reader.getBlocks().isEmpty()) {
                var diagram = reader.getBlocks().get(0).getDiagram();
                diagnostic = diagram.getWarningOrError() == null ? "" : diagram.getWarningOrError();
            }
            if ((renderDescription != null && renderDescription.toLowerCase().contains("syntax error"))
                    || diagnostic.toLowerCase().contains("syntax error")) {
                throw new IllegalStateException("PlantUML rejected generated source: " + diagnostic);
            }
            Image rendered = new Image(new ByteArrayInputStream(out.toByteArray()));
            if (rendered.isError()) throw new IllegalStateException("Image decode failed", rendered.getException());
            if (isPlantUmlErrorPage(rendered)) {
                throw new IllegalStateException("PlantUML returned its syntax-error page. Open Source to inspect the generated diagram.");
            }
            image.setImage(rendered);
            resizeDiagram();
            if (!showingSource) visual.getChildren().setAll(scroll);
            status.setText("UML · " + (int) rendered.getWidth() + " × " + (int) rendered.getHeight()
                    + " · " + uml.lines().count() + " lines · " + Math.round(zoom.getValue() * 100) + "%");
        } catch (Exception ex) {
            Label error = new Label("PlantUML rendering failed\n" + ex.getMessage()
                    + "\nUse Source to inspect the generated UML.");
            error.setWrapText(true);
            error.setMaxWidth(Double.MAX_VALUE);
            error.setStyle("-fx-text-fill:#fca5a5;-fx-padding:18;-fx-font-size:12px;");
            if (!showingSource) visual.getChildren().setAll(error);
            status.setText("UML render error · " + ex.getClass().getSimpleName());
        }
    }

    private static boolean isPlantUmlErrorPage(Image image) {
        var reader = image.getPixelReader();
        if (reader == null) return false;
        int width = (int) image.getWidth(), height = (int) image.getHeight();
        long sampled = 0, red = 0;
        int step = Math.max(1, Math.max(width, height) / 500);
        for (int y = 0; y < height; y += step) {
            for (int x = 0; x < width; x += step) {
                int argb = reader.getArgb(x, y);
                int r = (argb >>> 16) & 255, g = (argb >>> 8) & 255, b = argb & 255;
                sampled++;
                if (r > 175 && g < 115 && b < 115) red++;
            }
        }
        return sampled > 0 && (double) red / sampled > 0.00035;
    }
}
