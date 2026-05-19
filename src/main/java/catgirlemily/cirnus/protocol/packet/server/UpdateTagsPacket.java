package catgirlemily.cirnus.protocol.packet.server;

import catgirlemily.cirnus.protocol.PacketBuffer;

import java.io.*;

public class UpdateTagsPacket {

    public static final int PACKET_ID = 0x0D;

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buf = PacketBuffer.newBuffer();
        PacketBuffer.writeVarInt(buf, 0); // empty map
        PacketBuffer.sendPacket(out, PACKET_ID, buf.toByteArray());
    }
}