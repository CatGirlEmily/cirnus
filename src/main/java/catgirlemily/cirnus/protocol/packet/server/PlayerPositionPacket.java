package catgirlemily.cirnus.protocol.packet.server;

import catgirlemily.cirnus.protocol.PacketBuffer;

import java.io.*;

public class PlayerPositionPacket {

    public static final int PACKET_ID = 0x64;

    private final double x, y, z;
    private final float yRot, xRot;

    public PlayerPositionPacket(double x, double y, double z, float yRot, float xRot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yRot = yRot;
        this.xRot = xRot;
    }

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buf = PacketBuffer.newBuffer();
        PacketBuffer.writeVarInt(buf, 1);        // teleport id
        PacketBuffer.writeDouble(buf, x);
        PacketBuffer.writeDouble(buf, y);
        PacketBuffer.writeDouble(buf, z);
        PacketBuffer.writeDouble(buf, 0);        // delta x
        PacketBuffer.writeDouble(buf, 0);        // delta y
        PacketBuffer.writeDouble(buf, 0);        // delta z
        PacketBuffer.writeFloat(buf, yRot);
        PacketBuffer.writeFloat(buf, xRot);
        PacketBuffer.writeInt(buf, 0);           // relatives: all absolute
        PacketBuffer.sendPacket(out, PACKET_ID, buf.toByteArray());
    }
}