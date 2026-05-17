package catgirlemily.cirnus.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Minimalistyczny logger z kolorami ANSI.
 * Nie potrzebujemy Log4j ani SLF4J na tym etapie.
 */
public class Logger {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Kody kolorów ANSI
    private static final String RESET  = "\u001B[0m";
    private static final String GRAY   = "\u001B[90m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String CYAN   = "\u001B[36m";

    private static String timestamp() {
        return GRAY + "[" + LocalTime.now().format(TIME_FMT) + "] " + RESET;
    }

    public static void info(String msg) {
        System.out.println(timestamp() + GREEN + "[INFO] " + msg);
    }

    public static void warn(String msg) {
        System.out.println(timestamp() + YELLOW + "[WARN] " + msg);
    }

    public static void error(String msg) {
        System.err.println(timestamp() + RED + "[ERROR] " + msg);
    }

    public static void error(String msg, Throwable t) {
        error(msg + " – " + t.getMessage());
        if (ServerConfig.DEBUG_PACKETS) t.printStackTrace();
    }

    public static void debug(String msg) {
        if (ServerConfig.DEBUG_PACKETS) System.out.println(timestamp() + CYAN + "[DEBUG] " + RESET + msg);
    }

    public static void connection(String address, String msg) {
        System.out.println(timestamp() + CYAN + "[CONN] " + RESET + GRAY + address + " " + RESET + msg);
    }
}
