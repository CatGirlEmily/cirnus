package catgirlemily.cirnus.network;

import catgirlemily.cirnus.protocol.ConnectionState;
import catgirlemily.cirnus.protocol.PacketBuffer;
import catgirlemily.cirnus.protocol.packet.client.*;
import catgirlemily.cirnus.protocol.packet.server.*;
import catgirlemily.cirnus.util.Logger;
import catgirlemily.cirnus.util.RegistryData;
import catgirlemily.cirnus.util.ServerConfig;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final Socket socket;
    private final String address;
    private final int id;

    private ConnectionState state = ConnectionState.HANDSHAKE;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.address = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        this.id = COUNTER.incrementAndGet();
    }

    @Override
    public void run() {
        Logger.connection(address, "connected [#" + id + "]");

        try (socket) {
            socket.setTcpNoDelay(!ServerConfig.NAGLE_ALGORITHM);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // packet loop
            while (!socket.isClosed()) handlePacket(in, out);
        } catch (EOFException | java.net.SocketException e) { Logger.connection(address, "disconnected [#" + id + "]");
        } catch (Exception e) { Logger.error("Connection Error " + address + " [#" + id + "]", e); }
    }

    private void handlePacket(InputStream in, OutputStream out) throws IOException {
        int length = PacketBuffer.readVarInt(in);
        int packetId = PacketBuffer.readVarInt(in);
        int payloadLen = length - PacketBuffer.varIntSize(packetId);
        byte[] payload = payloadLen > 0 ? in.readNBytes(payloadLen) : new byte[0];

        switch (state) {
            case HANDSHAKE -> onHandshake(packetId, payload);
            case STATUS -> onStatus(packetId, payload, out);
            case CONFIGURATION -> onConfiguration(packetId, payload, out);
            case LOGIN -> onLogin(packetId, payload, out);
        default-> Logger.warn("Unknown state: " + state);
        }
    }

    // -------------------------------------------------------------------------
    // HANDSHAKE
    // -------------------------------------------------------------------------

    private void onHandshake(int packetId, byte[] payload) throws IOException {
        if (packetId != 0x00) {
            Logger.warn("Unexpected packet HANDSHAKE: 0x%02X".formatted(packetId));
            return;
        }

        HandshakePacket packet = HandshakePacket.read(payload);
        Logger.debug("Handshake: protocol=%d address=%s:%d nextState=%d".formatted(packet.protocolVersion, packet.serverAddress,packet.serverPort, packet.nextState));

        state = switch (packet.nextState) {
            case 1  -> ConnectionState.STATUS;
            case 2  -> ConnectionState.LOGIN;
        default -> throw new IOException("Unknown nextState: " + packet.nextState);
        };
    }

    // -------------------------------------------------------------------------
    // STATUS
    // -------------------------------------------------------------------------

    private void onStatus(int packetId, byte[] payload, OutputStream out) throws IOException {
        switch (packetId) {
            case 0x00 -> StatusResponsePacket.create().send(out);
            case 0x01 -> {
                PingRequestPacket ping = PingRequestPacket.read(payload);
                Logger.debug("=> Pong " + ping.pingId);
                new PongResponsePacket(ping.pingId).send(out);
            }
        default -> Logger.warn("Unknown packet STATUS: 0x%02X".formatted(packetId));
        }
    }

    // ------------------------------------------------------------------------
    // CONFIGURATION
    // ------------------------------------------------------------------------

    private void onConfiguration(int packetId, byte[] payload, OutputStream out) throws IOException {
        switch (packetId) {
            case 0x07 -> {
                out.write(RegistryData.registries);
                out.write(RegistryData.tags);
                out.flush();
                new FinishConfigurationPacket().send(out);
            }
            case 0x03 -> {
                // ServerboundFinishConfiguration
                Logger.debug("Configuration finished, switching to PLAY");
                state = ConnectionState.PLAY;
                new LoginPlayPacket(1).send(out);
                new GameEventPacket(GameEventPacket.LEVEL_CHUNKS_LOAD_START, 0).send(out);
                new PlayerPositionPacket(0, 64, 0, 0, 0).send(out);
            }
        default -> Logger.debug("Ignored CONFIGURATION packet: 0x%02X".formatted(packetId));
        }
    }

    // -------------------------------------------------------------------------
    // LOGIN
    // -------------------------------------------------------------------------

    private void onLogin(int packetId, byte[] payload, OutputStream out) throws IOException {
        switch (packetId) {
            case 0x00 -> {
                LoginStartPacket packet = LoginStartPacket.read(payload);
                Logger.connection(address, "Login attempt: " + packet.username);
                UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + packet.username).getBytes());
                new LoginSuccessPacket(uuid, packet.username).send(out);
            }
            case 0x03 -> {
                Logger.debug("Login acknowledged!");
                state = ConnectionState.CONFIGURATION;
                new SelectKnownPacksPacket().send(out);
            }
        }
    }
}