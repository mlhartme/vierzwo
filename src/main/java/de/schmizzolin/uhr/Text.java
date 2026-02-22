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

    public Text() {
        updateFont();
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: black;");
        setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(10)
        )));

        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateFont();
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            updateFont();
        });
    }

    private void updateFont() {
        // we have 5 characters, each of them with 3x5 dots
        // we but have a dot between chars
        // -> display ration is 17:5
        int dotSize;
        if (getWidth() / getHeight() > 17.0 / 5.0) {
            dotSize = (int) (getHeight() / 5.0);
        } else {
            dotSize = (int) (getWidth() / 17.0);
        }
        this.font = Font.create(Color.WHITE, dotSize);
    }

    protected void setText(String text) {
        getChildren().setAll(font.render(text));
    }
}
