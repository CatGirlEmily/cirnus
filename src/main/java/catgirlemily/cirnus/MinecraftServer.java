package catgirlemily.cirnus;

import catgirlemily.cirnus.network.Server;
import catgirlemily.cirnus.util.RegistryData;

public class MinecraftServer {
    public static void main(String[] args) throws Exception {
        RegistryData.load();

        int port = 25565;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);
        server.start();
    }
}
