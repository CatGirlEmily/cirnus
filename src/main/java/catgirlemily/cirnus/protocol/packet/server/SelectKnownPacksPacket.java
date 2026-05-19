package catgirlemily.cirnus.protocol.packet.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import catgirlemily.cirnus.protocol.PacketBuffer;

public class SelectKnownPacksPacket {
    public static final int PACKET_ID = 0x0E;

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buf = PacketBuffer.newBuffer();
        PacketBuffer.writeVarInt(buf, 0);
        PacketBuffer.sendPacket(out, PACKET_ID, buf.toByteArray());
    }
}
