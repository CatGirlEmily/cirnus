package catgirlemily.cirnus.protocol.packet.server;

import catgirlemily.cirnus.protocol.PacketBuffer;

import java.io.*;

public class GameEventPacket {

    public static final int PACKET_ID = 0x26;
    public static final int LEVEL_CHUNKS_LOAD_START = 13;

    private final int event;
    private final float param;

    public GameEventPacket(int event, float param) {
        this.event = event;
        this.param = param;
    }

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buf = PacketBuffer.newBuffer();
        PacketBuffer.writeByte(buf, event);
        PacketBuffer.writeFloat(buf, param);
        PacketBuffer.sendPacket(out, PACKET_ID, buf.toByteArray());
    }
}