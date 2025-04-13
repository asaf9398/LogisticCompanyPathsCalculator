package main.java.com.hit.app;

import main.java.com.hit.algorithm.*;
import main.java.com.hit.server.Server;

public class Main {
    public static void main(String[] args) {
        IAlgoBestPathsCalculator algo = new DijkstraPathsCalcImpl();
        Server server = new Server(5000, algo);
        server.start();
    }
}
