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
    private String text;

    public Text() {
        this.font = Font.create(Color.WHITE);
        this.text = ":::";
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

    protected void setText(String text) {
        this.text = text;
    }

    private void updateSize() {
        // we have 5 characters, each of them with 3x5 dots
        // we but have a dot between chars
        // -> display ration is 17:5

        double xDots = font.width(text);
        double yDots = font.height();
        int dotSize;
        if (getWidth() / getHeight() > xDots / yDots) {
            dotSize = (int) (getHeight() / yDots);
        } else {
            dotSize = (int) (getWidth() / xDots);
        }
        getChildren().setAll(font.render(text, dotSize));
    }
}
