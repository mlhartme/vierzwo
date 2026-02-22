package de.schmizzolin.uhr;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Locale;

// Beispiel für eine eigene Pane mit automatischer Aktualisierung
public class Wetter extends Text {
    private String text;

    public Wetter() throws IOException, InterruptedException {
        super();

        var today = new Dwd().stationOverviewExtended();
        this.text = round(today.temperaturMin()) + ":" + round(today.temperatureMax());
    }

    protected void layoutChildren() {
        System.out.println("layoutChildren" + getChildren().size());
        setText(text);
        super.layoutChildren();
    }


    private static int round(int temp) {
        return (temp + 5) / 10;
    }
}
