package catgirlemily.cirnus.protocol.packet.server;

import catgirlemily.cirnus.protocol.PacketBuffer;

import java.io.*;


public class FinishConfigurationPacket {
    public static final int PACKET_ID = 0x03;

    public void send(OutputStream out) throws IOException {
        PacketBuffer.sendPacket(out, PACKET_ID, new byte[0]);
    }
}