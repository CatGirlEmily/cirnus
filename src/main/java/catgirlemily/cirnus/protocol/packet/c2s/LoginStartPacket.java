package catgirlemily.cirnus.protocol.packet.c2s;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import catgirlemily.cirnus.protocol.PacketBuffer;


public class LoginStartPacket {
    public final String username;

    private LoginStartPacket(String username) {
        this.username = username;
    }

    public static LoginStartPacket read(byte[] payload) throws IOException {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload));
        return new LoginStartPacket(PacketBuffer.readString(data));
    }
}
