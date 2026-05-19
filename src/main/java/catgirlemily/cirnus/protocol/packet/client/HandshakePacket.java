package catgirlemily.cirnus.protocol.packet.client;

import java.io.*;

import catgirlemily.cirnus.protocol.PacketBuffer;
 

public class HandshakePacket {
    public final int protocolVersion;
    public final String serverAddress;
    public final int serverPort;
    public final int nextState;
 
    private HandshakePacket(int protocolVersion, String serverAddress, int serverPort, int nextState) {
        this.protocolVersion = protocolVersion;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.nextState = nextState;
    }
 
    public static HandshakePacket read(byte[] payload) throws IOException {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload));
        int protocolVersion = PacketBuffer.readVarInt(data);
        String serverAddress = PacketBuffer.readString(data);
        int serverPort = (data.readUnsignedByte() << 8) | data.readUnsignedByte();
        int nextState = PacketBuffer.readVarInt(data);
        return new HandshakePacket(protocolVersion, serverAddress, serverPort, nextState);
    }
}
 