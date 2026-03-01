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

* weather symbols
  * complete
  * beautify
* current temperature

* centered lines
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
