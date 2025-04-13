package main.java.com.hit.algorithm;

import java.util.*;

import java.util.*;

public class BellmanFordPathsCalcImpl implements IAlgoBestPathsCalculator {

    @Override
    public List<Node> calculateShortestPaths(MapGraph graph, Node source) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        List<Node> result = new ArrayList<>();

        for (Node v : graph.getNodeList()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);

        for (int i = 0; i < graph.getNodeList().size() - 1; i++) {
            for (Node current : graph.getNodeList()) {
                for (Map.Entry<String, Double> edge : current.getEdgesList().entrySet()) {
                    Node neighbor = graph.findNodeById(edge.getKey());
                    double weight = edge.getValue();
                    double newDistance = distances.get(current) + weight;

                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        predecessors.put(neighbor, current);
                    }
                }
            }
        }

        for (Node current : graph.getNodeList()) {
            for (Map.Entry<String, Double> edge : current.getEdgesList().entrySet()) {
                Node neighbor = graph.findNodeById(edge.getKey());
                if (distances.get(current) + edge.getValue() < distances.get(neighbor)) {
                    throw new RuntimeException("Graph contains a negative weight cycle!");
                }
            }
        }

        for (Map.Entry<Node, Double> entry : distances.entrySet()) {
            if (!entry.getValue().equals(Double.POSITIVE_INFINITY)) {
                result.add(entry.getKey());
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
            Node source = graph.getNodeList().get(i);
            for (int j = 0; j < size; j++) {
                Node target = graph.getNodeList().get(j);
                if (i == j) {
                    distanceMatrix[i][j] = 0.0;
                } else {
                    distanceMatrix[i][j] = computeShortestPath(graph, source, target);
                }
            }
        }
        return distanceMatrix;
    }

    private double computeShortestPath(MapGraph graph, Node source, Node target) {

        if (source.equals(target)) {
            return 0.0;
        }

        Map<Node, Double> distances = new LinkedHashMap<>();
        for (Node v : graph.getNodeList()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);

        int n = graph.getNodeList().size();
        for (int i = 0; i < n - 1; i++) {
            for (Node current : graph.getNodeList()) {
                for (Map.Entry<String, Double> edge : current.getEdgesList().entrySet()) {
                    Node neighbor = graph.findNodeById(edge.getKey());
                    double weight = edge.getValue();
                    double newDistance = distances.get(current) + weight;
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                    }
                }
            }
        }

        return distances.get(target);
    }
}
