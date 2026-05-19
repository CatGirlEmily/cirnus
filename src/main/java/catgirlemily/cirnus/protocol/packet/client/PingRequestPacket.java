package catgirlemily.cirnus.protocol.packet.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import catgirlemily.cirnus.protocol.PacketBuffer;


public class PingRequestPacket {
    public final long pingId;

    private PingRequestPacket(long pingId) {
        this.pingId = pingId;
    }

    public static PingRequestPacket read(byte[] payload) throws IOException {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload));
        return new PingRequestPacket(PacketBuffer.readLong(data));
    }
}