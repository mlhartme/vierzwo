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

* centered lines
* weather
  * https://dwd.api.bund.dev
  * https://www.dwd.de/DE/leistungen/klimadatendeutschland/stationsuebersicht.html
  *  curl -X 'GET' \
     'https://dwd.api.proxy.bund.dev/v30/stationOverviewExtended?stationIds=10501,G3' \
     -H 'accept: application/json' | jq > aachen.json
  * under "days"
    * temperaturen in 1/10 grad
    * precipitation: nederschlag in 1/10 mm
    * sunshine: in 1/10 minuten -> dh /600 ergibt stunden
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
