# cirnus - custom minecraft server from scratch
networking nie wydaje sie zly a mialam od jakiegos czasu ochote na zrobienie wlasnego serwera
inspirowane oczywiscie portalrunner (jest w moich starred)
potem moge robic jakies glupie rzeczy z tym bez potrzeby *pierdolenia* sie 5 godzin z mixinami

## Current state

| Function | Status |
|---|---|
| Handshake | ✅ |
| Status / MOTD | ✅ |
| Pong (latency) | ✅ |
| Login Start + Disconnect | ✅ |
| Login Success / PLAY | ⏳ to be added |

shoutout claude for these emojis

## Dependencies

- **Java 21+** (`java -version`)
- **Gradle 8+** (`gradle --version`) or use Graddle Wrapper

## Launching

```bash
# Dev mode (without jar build)
./gradlew run

# Build fat jar & launch
./gradlew jar
java -jar build/libs/cirnus.jar

# Optionally – different port (defaults to 25565)
java -jar build/libs/cirnus.jar 25566
```

## Docs used

- **Packets**: https://minecraft.wiki/w/Java_Edition_protocol/Packets
- **Data types**: https://minecraft.wiki/w/Java_Edition_protocol/Data_types
- **FAQ**: https://minecraft.wiki/w/Java_Edition_protocol/FAQ

## todo
server.properties / better configuration