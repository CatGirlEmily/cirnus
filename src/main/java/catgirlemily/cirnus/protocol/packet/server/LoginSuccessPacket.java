package catgirlemily.cirnus.protocol.packet.server;

import catgirlemily.cirnus.protocol.PacketBuffer;

import java.io.*;
import java.util.UUID;

public class LoginSuccessPacket {
    public static final int PACKET_ID = 0x02;

    private final UUID uuid;
    private final String username;

    public LoginSuccessPacket(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buf = PacketBuffer.newBuffer();
        PacketBuffer.writeUUID(buf, uuid);
        PacketBuffer.writeString(buf, username);
        PacketBuffer.writeVarInt(buf, 0); // empty properties
        PacketBuffer.sendPacket(out, PACKET_ID, buf.toByteArray());
    }
}