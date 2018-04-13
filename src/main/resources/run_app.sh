stty -echo raw
java -cp . whorten.termgames.GameConsole || true
stty echo cooked
clear