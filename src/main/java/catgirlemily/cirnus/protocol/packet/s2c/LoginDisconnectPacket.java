package catgirlemily.cirnus.protocol.packet.s2c;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import catgirlemily.cirnus.protocol.PacketBuffer;

public class LoginDisconnectPacket {

    public static final int PACKET_ID = 0x00;

    private final String reason;

    public LoginDisconnectPacket(String reason) {
        this.reason = "{\"text\":\"%s\"}".formatted(reason);
    }

    public void send(OutputStream out) throws IOException {
        ByteArrayOutputStream buffer = PacketBuffer.newBuffer();
        PacketBuffer.writeString(buffer, reason);
        PacketBuffer.sendPacket(out, PACKET_ID, buffer.toByteArray());
    }
}