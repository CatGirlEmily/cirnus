package catgirlemily.cirnus.util;

import java.io.*;

public class RegistryData {
    public static byte[] registries;
    public static byte[] tags;

    public static void load() throws IOException {
        registries = RegistryData.class.getResourceAsStream("/registries.bin").readAllBytes();
        tags = RegistryData.class.getResourceAsStream("/tags.bin").readAllBytes();
        Logger.info("Loaded registries (" + registries.length + " bytes)");
        Logger.info("Loaded tags (" + tags.length + " bytes)");
    }
}