stty -echo raw
java -cp . whorten.termgames.App || true
stty echo cooked