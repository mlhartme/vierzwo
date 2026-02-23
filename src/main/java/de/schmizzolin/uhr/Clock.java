package de.schmizzolin.uhr;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Locale;

// Beispiel für eine eigene Pane mit automatischer Aktualisierung
public class Clock extends Text {
    private final String temperature;
    public Clock() throws IOException, InterruptedException {
        super();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateTime()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();

        var today = new Dwd().stationOverviewExtended();
        temperature = "v" + round(today.temperaturMin()) + " " + round(today.temperatureMax()) + "^";
    }

    private static int round(int temp) {
        return (temp + 5) / 10;
    }

    private void updateTime() {
        LocalTime now = LocalTime.now();
        String time = String.format(Locale.GERMAN, "%02d:%02d", now.getHour(), now.getMinute());
        setText(time, temperature);
    }
}
