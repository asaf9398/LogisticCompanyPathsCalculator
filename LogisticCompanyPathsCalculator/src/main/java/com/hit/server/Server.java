package main.java.com.hit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import main.java.com.hit.algorithm.*;

/**

 */
public class Server {
    private int port;
    private IAlgoBestPathsCalculator algo;
    private ExecutorService threadPool;
    private ServerSocket serverSocket;


    public Server(int port, IAlgoBestPathsCalculator algo) {
        this.port = port;
        this.algo = algo;
        this.threadPool = Executors.newFixedThreadPool(5);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                threadPool.execute(new ClientHandler(clientSocket, algo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
