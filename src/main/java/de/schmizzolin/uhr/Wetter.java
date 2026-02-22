package de.schmizzolin.uhr;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Locale;

// Beispiel für eine eigene Pane mit automatischer Aktualisierung
public class Wetter extends Text {
    public Wetter() throws IOException, InterruptedException {
        super();

        var today = new Dwd().stationOverviewExtended();
        setText(round(today.temperaturMin()) + ":" + round(today.temperatureMax()));
    }

    private static int round(int temp) {
        return (temp + 5) / 10;
    }
}
