# TerminalGames
collection of games meant to be played in a terminal emulator

## How to build and run TermGames

git clone https://github.com/sabotuer99/TerminalGames.git

cd TerminalGames

mvn package

./run_app.sh

## Known Issues

Ubuntu uses PulseAudio by default, which is not compatible with the Oracle JRE. Use OpenJdk and the IcedTea plugin.
