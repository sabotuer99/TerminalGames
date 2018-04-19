stty -echo raw
java -XX:+UseG1GC -Xms600m -cp . whorten.termgames.GameConsole || true
stty echo cooked
clear