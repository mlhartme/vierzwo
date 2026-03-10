package de.schmizzolin.uhr;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Locale;

public class Clock extends Text {
    private OffsetDateTime todayUpdate;
    private Dwd.Today today;

    public Clock(){
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
        var time = String.format(Locale.GERMAN, "%02d:%02d", now.getHour(), now.getMinute());
        var temperature = round(today.temperaturMin()) + "-" + round(today.temperatureMax()) + "dc";
        var rainSun  = "s" + today.sunshineHours() + "h p" + today.precipitationMM() + "mm";

        setText(time, Character.toString((char) ('@' + today.icon())), temperature, rainSun);
    }

    private void updateWeather() throws IOException, InterruptedException {
        if (todayUpdate.isBefore(OffsetDateTime.now().minusHours(1))) {
            today = new Dwd().stationOverviewExtendedToday(WeatherStation.AACHEN);
            todayUpdate = OffsetDateTime.now();
            System.out.println("icon: " + today.icon());
        }

    }
}
