package de.schmizzolin.uhr;

import java.io.IOException;

// Beispiel für eine eigene Pane mit automatischer Aktualisierung
public class Weather extends Text {
    public Weather() throws IOException, InterruptedException {
        super();

        var today = new Dwd().stationOverviewExtended();
        setText("v" + round(today.temperaturMin()) + " " + round(today.temperatureMax()) + "^");
    }

    private static int round(int temp) {
        return (temp + 5) / 10;
    }
}
