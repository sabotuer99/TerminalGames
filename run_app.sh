stty -echo raw
export log="$(pwd)"
runtime_directory="$(dirname $(readlink -f $0))/target"
java -XX:+UseG1GC -Xms600m -jar ${runtime_directory}/GameConsole.jar || true
log=
stty echo cooked
clear