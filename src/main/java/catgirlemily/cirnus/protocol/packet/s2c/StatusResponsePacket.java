package catgirlemily.cirnus.protocol.packet.s2c;

import catgirlemily.cirnus.protocol.PacketBuffer;
import catgirlemily.cirnus.util.ServerConfig;

import java.io.*;


public class StatusResponsePacket {
    public static final int PACKET_ID = 0x00;

    private final String json;

    private StatusResponsePacket(String json) {
        this.json = json;
    }

    // this stinks
    public static StatusResponsePacket create() {
        String json = """
                {
                  "version": { "name": "%s", "protocol": %d },
                  "players": { "max": %d, "online": 0, "sample": [] },
                  "description": { "text": "%s" }
                }
                """.formatted(
                ServerConfig.VERSION_NAME,
                ServerConfig.PROTOCOL_VERSION,
                ServerConfig.MAX_PLAYERS,
                ServerConfig.MOTD
        );
        return new StatusResponsePacket(json);
    }

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buf = PacketBuffer.newBuffer();
        PacketBuffer.writeString(buf, json);
        PacketBuffer.sendPacket(out, PACKET_ID, buf.toByteArray());
    }
}