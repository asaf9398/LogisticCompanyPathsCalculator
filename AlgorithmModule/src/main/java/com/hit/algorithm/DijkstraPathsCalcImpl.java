package main.java.com.hit.algorithm;

import javax.print.DocFlavor;
import java.util.*;

public class DijkstraPathsCalcImpl implements IAlgoBestPathsCalculator {

    @Override
    public List<Node> calculateShortestPaths(MapGraph graph, Node source) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(distances::get));
        List<Node> result = new ArrayList<>();
        Set<Node> visited = new HashSet<>();

        for (Node v : graph.getNodeList()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);
        queue.add(source);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            result.add(current);

            for (Map.Entry<String, Double> edge : current.getEdgesList().entrySet()) {
                Node neighbor = graph.findNodeById(edge.getKey());
                double weight = edge.getValue();
                double newDistance = distances.get(current) + weight;

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return result;
    }

    @Override
    public List<Node> getShortestPath(Node destination, Map<Node, Node> predecessors) {
        List<Node> path = new ArrayList<>();
        Node step = destination;

        while (step != null) {
            path.add(0, step);
            step = predecessors.get(step);
        }

        return path;
    }

    @Override
    public double[][] calculateDistanceMatrix(MapGraph graph) {
        int size = graph.getNodeList().size();
        double[][] distanceMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    Node src = graph.getNodeList().get(i);
                    Node dst = graph.getNodeList().get(j);
                    distanceMatrix[i][j] = computeShortestPath(graph, src, dst);
                }
            }
        }
        return distanceMatrix;
    }

    private double computeShortestPath(MapGraph graph, Node source, Node target) {
        Map<Node, Double> distances = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

        for (Node v : graph.getNodeList()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);
        queue.add(source);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.equals(target)) {
                break;
            }

            for (Map.Entry<String, Double> edge : current.getEdgesList().entrySet()) {
                Node neighbor = graph.findNodeById(edge.getKey());
                double weight = edge.getValue();
                double newDistance = distances.get(current) + weight;
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    queue.add(neighbor);
                }
            }
        }
        return distances.get(target);
    }
}
