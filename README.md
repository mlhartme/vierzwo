# Uhr

## Setup

* Download JavaFX SDK 21+ from [Gluon](https://gluonhq.com/products/javafx/)
* unpack to /some/path/javafx-sdk
* export JAVA_FX_HOME=/some/path/javafx-sdk

## Build

with

    mvn clean package

## Run

from shell with

    ./target/uhr

and from IntelliJ IDEA with VM options

    --module-path $JAVA_FX_HOME/lib --add-modules javafx.controls,javafx.fxml

## TODO

* wether
  * https://dwd.api.bund.dev
  * https://www.dwd.de/DE/leistungen/klimadatendeutschland/stationsuebersicht.html
  *  curl -X 'GET' \
     'https://dwd.api.proxy.bund.dev/v30/stationOverviewExtended?stationIds=10501,G3' \
     -H 'accept: application/json' | jq > aachen.json
  * unter "days"
    * temperaturen in 1/10 grad
    * precipitation: nederschlag in 1/10 mm
    * sunshine: in 1/10 minuten -> dh /600 ergibt stunden
    * icon
      * https://listed.to/@DieSieben/7851/api-des-deutschen-wetterdienstes
      * https://content.meteoblue.com/de/forschung-bildung/spezifikationen/standards/symbole-und-piktogramme
* beautify dots
* animate
* fancy name
  * PiMessage 
  * Glance
  * Summary 
  * Zusammenfassung
  * tldr
  * extract
  * Zweiundvierzig
  * Vierzwo
