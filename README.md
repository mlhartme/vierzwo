# Vierzwo

Display answers.

## Build

with

    mvn clean package

## Run

from shell with

    ./target/vierzwo 

and from IntelliJ IDEA by running `de.schmizzolin.vierzwo.Main`.

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

* weather symbols
  * complete
  * beautify
* beautify dots
* animate
