package de.schmizzolin.uhr;

import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

// Beispiel für eine eigene Pane mit automatischer Aktualisierung
public class Text extends StackPane {
    private Font font;
    private String[] text;

    public Text() {
        this.font = Font.create();
        this.text = new String[0];
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: black;");
        setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(10)
        )));
        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateSize();
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            updateSize();
        });
    }

    protected void setText(String... text) {
        this.text = text;
    }

    protected void updateSize() {
        double xDots = font.width(text);
        double yDots = font.height(text);
        int dotSize;
        if (getWidth() / getHeight() > xDots / yDots) {
            dotSize = (int) (getHeight() / yDots);
        } else {
            dotSize = (int) (getWidth() / xDots);
        }
        getChildren().setAll(font.render(dotSize, text));
    }
}
