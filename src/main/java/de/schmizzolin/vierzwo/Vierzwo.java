package de.schmizzolin.vierzwo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Locale;

public class Vierzwo extends Text {
    private OffsetDateTime todayUpdate;
    private Dwd.Today today;
    private int currentTemperatur;

    public Vierzwo(){
        super();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> {
                    try {
                        updateTime();
                    } catch (IOException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
        todayUpdate = OffsetDateTime.now().minusHours(100);
    }

    private static int round(int temp) {
        return (temp + 5) / 10;
    }

    private void updateTime() throws IOException, InterruptedException {
        updateWeather();

        var now = LocalDateTime.now();
        var date = String.format(Locale.GERMAN, "%d.%d. %d", now.getDayOfMonth(), now.getMonth().getValue(), now.getYear());
        var time = String.format(Locale.GERMAN, "%02d:%02d", now.getHour(), now.getMinute());
        var rainSun  = "s" + today.sunshineHours() + "h p" + today.precipitationMM() + "mm";
        var temperature = round(today.temperaturMin()) + "d "
                + currentTemperatur + "d "
                + round(today.temperatureMax()) + "d";

        setText(date, time, Character.toString((char) ('@' + today.icon())), rainSun, temperature);
    }

    private void updateWeather() throws IOException, InterruptedException {
        if (todayUpdate.isBefore(OffsetDateTime.now().minusHours(1))) {
            today = new Dwd().stationOverviewExtendedToday(WeatherStation.AACHEN);
            todayUpdate = OffsetDateTime.now();
            currentTemperatur = new BrightSky().temperatur(50.767897, 6.121299);
            System.out.println("icon: " + today.icon());
        }
    }
}
