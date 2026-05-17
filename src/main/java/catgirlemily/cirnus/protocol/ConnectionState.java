package catgirlemily.cirnus.protocol;

/**
 * Connection states in java edition protocoles
 * Every connection begins in Handshake and procceeds further
 * depending on Handshake's content
 *
 * learn more:
 * https://minecraft.wiki/w/Java_Edition_protocol#Connection_states
 */

public enum ConnectionState {
    HANDSHAKE,  // hello from client
    STATUS,     // ping / server list
    LOGIN,      // player login
    PLAY        // actual game (not implemented yet)
}
