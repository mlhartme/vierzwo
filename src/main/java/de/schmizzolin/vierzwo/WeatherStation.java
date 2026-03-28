package de.schmizzolin.vierzwo;

// from https://www.dwd.de/DE/leistungen/klimadatendeutschland/stationsuebersicht.html
public enum WeatherStation {
    AACHEN(10501, 3),
    BREMEN(10224, 691),
    KIEL_HOLTENAU(10046, 2564);

    private final int kennziffer;
    private final int id;

    WeatherStation(int kennziffer, int id) {
        this.kennziffer = kennziffer;
        this.id = id;
    }

    public int kennziffer() {
        return kennziffer;
    }

    public int id() {
        return id;
    }
}
