package catgirlemily.cirnus.protocol.packet.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import catgirlemily.cirnus.protocol.PacketBuffer;

public class LoginStartPacket {

    public final String username;
    public final java.util.UUID profileId;

    private LoginStartPacket(String username, java.util.UUID profileId) {
        this.username = username;
        this.profileId = profileId;
    }

    public static LoginStartPacket read(byte[] payload) throws IOException {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload));
        String username = PacketBuffer.readString(data);
        java.util.UUID profileId = PacketBuffer.readUUID(data);
        return new LoginStartPacket(username, profileId);
    }
}