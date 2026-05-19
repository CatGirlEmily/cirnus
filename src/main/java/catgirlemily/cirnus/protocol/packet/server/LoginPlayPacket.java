package catgirlemily.cirnus.protocol.packet.server;

import catgirlemily.cirnus.protocol.PacketBuffer;

import java.io.*;

public class LoginPlayPacket {

    public static final int PACKET_ID = 0x31;

    private final int playerId;

    public LoginPlayPacket(int playerId) {
        this.playerId = playerId;
    }

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buf = PacketBuffer.newBuffer();

        PacketBuffer.writeInt(buf, playerId);
        PacketBuffer.writeBoolean(buf, false);  // hardcore
        PacketBuffer.writeVarInt(buf, 1);       // dimension count
        PacketBuffer.writeString(buf, "minecraft:overworld");
        PacketBuffer.writeVarInt(buf, 20);      // max players
        PacketBuffer.writeVarInt(buf, 10);      // chunk radius
        PacketBuffer.writeVarInt(buf, 8);       // simulation distance
        PacketBuffer.writeBoolean(buf, false);  // reduced debug info
        PacketBuffer.writeBoolean(buf, true);   // show death screen
        PacketBuffer.writeBoolean(buf, false);  // do limited crafting

        // CommonPlayerSpawnInfo
        PacketBuffer.writeVarInt(buf, 0);         // dimension type
        PacketBuffer.writeString(buf, "minecraft:overworld");
        PacketBuffer.writeLong(buf, 0L);          // seed
        PacketBuffer.writeByte(buf, 1);                 // game type: creative
        PacketBuffer.writeByte(buf, -1);                // previous game type
        PacketBuffer.writeBoolean(buf, false);   // is debug
        PacketBuffer.writeBoolean(buf, false);   // is flat
        PacketBuffer.writeBoolean(buf, false);   // has death location
        PacketBuffer.writeVarInt(buf, 0);        // portal cooldown
        PacketBuffer.writeVarInt(buf, 63);       // sea level

        PacketBuffer.writeBoolean(buf, false);   // enforces secure chat

        PacketBuffer.sendPacket(out, PACKET_ID, buf.toByteArray());
    }
}