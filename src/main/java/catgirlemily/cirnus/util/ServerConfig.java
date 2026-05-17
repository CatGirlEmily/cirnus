package catgirlemily.cirnus.util;

/**
 * Centralna konfiguracja serwera.
 * Na razie hardkodowana – docelowo można wczytywać z pliku .properties.
 */
public class ServerConfig {
    public static final int PROTOCOL_VERSION = 775;
    public static final String VERSION_NAME = "26.1.2";
    public static final int MAX_PLAYERS = 20;
    public static final String MOTD = "wow serwer";
    public static final String FAVICON = null;

    // mid level
    public static final boolean DEBUG_PACKETS = false;

    // very low level
    public static final boolean NAGLE_ALGORITHM = false;
}
