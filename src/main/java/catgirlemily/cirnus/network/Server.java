package catgirlemily.cirnus.network;

import catgirlemily.cirnus.util.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public Server(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // usually port stays open for 60s after closing server. with this we can force close the port instantly
            // and restart server as soon as possible
            serverSocket.setReuseAddress(true);

            Logger.info("Server started!");

            while (true) {
                Socket client = serverSocket.accept();
                executor.submit(new ClientHandler(client));
            }
        }
    }
}
