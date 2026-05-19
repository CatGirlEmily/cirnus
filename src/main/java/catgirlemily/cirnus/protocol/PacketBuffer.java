package catgirlemily.cirnus.protocol;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Wrapper on byte streams with methods for reading/writing
 * data types used in the Minecraft Java Edition protocol.
 *
 * Type documentation: https://minecraft.wiki/w/Java_Edition_protocol/Data_types
 */
public class PacketBuffer {

    // -------------------------------------------------------------------------
    // VarInt
    // VarInt is an integer stored in 1-5 bytes.
    // Each byte: 7 bits of data + 1 bit "is there a next byte".
    // -------------------------------------------------------------------------

    public static int readVarInt(InputStream in) throws IOException {
        int value = 0;
        int position = 0;

        while (true) {
            int bytee = in.read();
            if (bytee == -1) throw new EOFException("End of stream while reading VarInt");

            value |= (bytee & 0x7F) << position;

            if ((bytee & 0x80) == 0) break;

            position += 7;
            if (position >= 32) throw new IOException("VarInt exceeded 4 byte limit.");
        }

        return value;
    }

    public static void writeVarInt(OutputStream out, int value) throws IOException {
        while (true) {
            if ((value & ~0x7F) == 0) {
                out.write(value);
                return;
            }
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }
    }

    public static int varIntSize(int value) {
        if ((value & (0xFFFFFFFF << 7)) == 0) return 1;
        if ((value & (0xFFFFFFFF << 14)) == 0) return 2;
        if ((value & (0xFFFFFFFF << 21)) == 0) return 3;
        if ((value & (0xFFFFFFFF << 28)) == 0) return 4;
        return 5;
    }

    // -------------------------------------------------------------------------
    // String (VarInt length + UTF-8 bytes)
    // -------------------------------------------------------------------------

    public static String readString(InputStream in) throws IOException {
        int length = readVarInt(in);
        byte[] bytes = in.readNBytes(length);
        if (bytes.length != length) throw new EOFException("Truncated string");
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeString(OutputStream out, String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeVarInt(out, bytes.length);
        out.write(bytes);
    }

    // -------------------------------------------------------------------------
    // Long (big-endian, 8 bytes)
    // -------------------------------------------------------------------------

    public static long readLong(InputStream in) throws IOException {
        byte[] b = in.readNBytes(8);
        if (b.length != 8) throw new EOFException("Truncated long");
        long v = 0;
        for (int i = 0; i < 8; i++) v = (v << 8) | (b[i] & 0xFF);
        return v;
    }

    public static void writeLong(OutputStream out, long value) throws IOException {
        for (int i = 7; i >= 0; i--) out.write((int) (value >> (8 * i)) & 0xFF);
    }

    // ---------------------------------------------------------------------------
    // Boolean (1 byte i guess?)
    // ---------------------------------------------------------------------------

    public static boolean readBoolean(InputStream in) throws IOException {
        return in.read() == 1;
    }

    public static void writeBoolean(OutputStream out, boolean value) throws IOException {
        out.write(value ? 1 : 0);
    }


    // ----------------------------------------------------------------------------
    // Int
    // -------------------------------------------------------------------------
    
    public static void writeInt(OutputStream out, int value) throws IOException {
        out.write((value >> 24) & 0xFF);
        out.write((value >> 16) & 0xFF);
        out.write((value >> 8) & 0xFF);
        out.write(value & 0xFF);
    }

    // -------------------------------------------------------------------------
    // Byte
    // -------------------------------------------------------------------------
    
    public static void writeByte(OutputStream out, int value) throws IOException {
        out.write(value & 0xFF);
    }

    // -------------------------------------------------------------------------
    // Float
    // -------------------------------------------------------------------------

    public static void writeFloat(OutputStream out, float value) throws IOException {
        writeInt(out, Float.floatToIntBits(value));
    }
    
    // -------------------------------------------------------------------------
    // Double
    // -------------------------------------------------------------------------
    
    public static void writeDouble(OutputStream out, double value) throws IOException {
        writeLong(out, Double.doubleToLongBits(value));
    }

    // --------------------------------------------------------------------------
    // UUID (2x Long)
    // --------------------------------------------------------------------------
    
    public static UUID readUUID(InputStream in) throws IOException {
        return new UUID(readLong(in), readLong(in));
    }

    public static void writeUUID(OutputStream out, UUID uuid) throws IOException {
        writeLong(out, uuid.getMostSignificantBits());
        writeLong(out, uuid.getLeastSignificantBits());
    }

    // -------------------------------------------------------------------------
    // Helpers for building a packet (id + payload) with a pre-calculated length
    // -------------------------------------------------------------------------

    /**
     * Writes a complete packet to {@code out}:
     *   [VarInt: total length] [VarInt: packet id] [payload]
     */
    public static void sendPacket(OutputStream out, int packetId, byte[] payload) throws IOException {
        int idSize = varIntSize(packetId);
        int totalLength = idSize + payload.length;

        writeVarInt(out, totalLength);
        writeVarInt(out, packetId);
        out.write(payload);
        out.flush();
    }

    public static ByteArrayOutputStream newBuffer() {
        return new ByteArrayOutputStream();
    }
}