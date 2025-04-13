package main.java.com.hit.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.com.hit.algorithm.MapGraph;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBManager {
    private static final String DB_FILE = "src/main/resources/datasource.txt";
    private static final Gson gson = new Gson();
    private static final Lock lock = new ReentrantLock();

    public static List<MapGraph> loadGraphsFromFile() {
        lock.lock();
        try {
            File f = new File(DB_FILE);
            if (!f.exists()) {
                return new ArrayList<>();
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }

            if (sb.length() == 0) {
                return new ArrayList<>();
            }

            Type listType = new TypeToken<List<MapGraph>>() {}.getType();
            return gson.fromJson(sb.toString(), listType);

        } finally {
            lock.unlock();
        }
    }

    public static void saveGraphsToFile(List<MapGraph> graphs) {
        lock.lock();
        try {
            File f = new File(DB_FILE);
            f.getParentFile().mkdirs();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                String json = gson.toJson(graphs);
                bw.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    public static void addGraph(MapGraph graph) {
        List<MapGraph> graphs = loadGraphsFromFile();
        graphs.add(graph);
        saveGraphsToFile(graphs);
    }
}
