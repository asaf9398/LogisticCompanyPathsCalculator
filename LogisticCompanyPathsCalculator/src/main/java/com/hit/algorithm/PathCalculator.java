package main.java.com.hit.algorithm;

import java.util.*;

public class PathCalculator {
    private IAlgoBestPathsCalculator algorithm;

    public PathCalculator(IAlgoBestPathsCalculator algorithm) {
        this.algorithm = algorithm;
    }

    public List<Node> findShortestPath(MapGraph graph, Node start, Node destination) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

        for (Node v : graph.getNodeList()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            for (Map.Entry<String, Double> edge : current.getEdgesList().entrySet()) {
                Node neighbor = graph.findNodeById(edge.getKey());
                double weight = edge.getValue();
                double newDistance = distances.get(current) + weight;
                if (neighbor!=null){
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current); //
                    queue.add(neighbor);
                }
                }
            }
        }

        if (!predecessors.containsKey(destination)) {
            return List.of();
        }

        return reconstructPath(predecessors, start, destination);
    }

    private List<Node> reconstructPath(Map<Node, Node> predecessors, Node start, Node destination) {
        List<Node> path = new ArrayList<>();
        Node step = destination;

        while (step != null) {
            path.add(0, step);
            step = predecessors.get(step);
        }

        return path;
    }
}
