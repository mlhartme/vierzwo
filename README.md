# Vierzwo

Display answers.

## Setup

* Download JavaFX SDK 21+ from [Gluon](https://gluonhq.com/products/javafx/)
* unpack to /some/path/javafx-sdk
* export JAVA_FX_HOME=/some/path/javafx-sdk

## Build

with

    mvn clean package

## Run

from shell with

    ./target/vierzwo 

and from IntelliJ IDEA with VM options

    --module-path $JAVA_FX_HOME/lib --add-modules javafx.controls,javafx.fxml

## Know how

* Rapsberry Pi OS has Wayland with Labwc Window manager
* simulate input
  * "ydotool" seems standard, but I didnt succeed wit apt install ydotool
  * use "evemu"
    * abt install evemu-tools
* launch from ssh terminal
  * export DISPLAY=:0

* for wakeup script: crontab -e

      */5 5-8 * * * /home/mhm/wakeup.sh

* Weather symbols
    https://listed.to/@DieSieben/7851/api-des-deutschen-wetterdienstes at the end
    https://content.meteoblue.com/de/forschung-bildung/spezifikationen/standards/symbole-und-piktogramme


## TODO

* https://brightsky.dev
* 
* current temperature
  * https://forum.iobroker.net/topic/70048/json-abfrage-per-request-liefert-nur-matsch
* date?
* weather symbols
  * complete
  * beautify
* beautify dots
* animate
