package de.schmizzolin.uhr;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.Locale;

// Beispiel für eine eigene Pane mit automatischer Aktualisierung
public class Clock extends Text {
    private Font font;

    public Clock() {
        super();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateTime()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    private void updateTime() {
        LocalTime time = LocalTime.now();
        String str = String.format(Locale.GERMAN, "%02d:%02d", time.getHour(), time.getMinute());
        setText(str);
    }
}
