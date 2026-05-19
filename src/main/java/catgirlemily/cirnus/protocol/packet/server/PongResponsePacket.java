package catgirlemily.cirnus.protocol.packet.server;

import catgirlemily.cirnus.protocol.PacketBuffer;

import java.io.*;

public class PongResponsePacket {
    public static final int PACKET_ID = 0x01;

    private final long pingId;

    public PongResponsePacket(long pingId) {
        this.pingId = pingId;
    }

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buf = PacketBuffer.newBuffer();
        PacketBuffer.writeLong(buf, pingId);
        PacketBuffer.sendPacket(out, PACKET_ID, buf.toByteArray());
    }
}