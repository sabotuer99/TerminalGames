stty -echo raw
export log="$(pwd)"
runtime_directory="$(dirname $(readlink -f $0))/target"
java -XX:+UseG1GC \
     -Xms600m \
     -jar ${runtime_directory}/GameConsole.jar || true
log=
stty echo cooked
clear
cd .

#     -Djavax.sound.sampled.Clip=com.sun.media.sound.DirectAudioDeviceProvider \
#     -Djavax.sound.sampled.Port=com.sun.media.sound.PortMixerProvider \
#     -Djavax.sound.sampled.SourceDataLine=com.sun.media.sound.DirectAudioDeviceProvider \
#     -Djavax.sound.sampled.TargetDataLine=com.sun.media.sound.DirectAudioDeviceProvider \