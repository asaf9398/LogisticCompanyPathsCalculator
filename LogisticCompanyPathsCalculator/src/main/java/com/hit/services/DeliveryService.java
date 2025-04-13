package main.java.com.hit.services;

import main.java.com.hit.algorithm.IAlgoBestPathsCalculator;
import main.java.com.hit.algorithm.Node;
import main.java.com.hit.algorithm.MapGraph;
import main.java.com.hit.algorithm.PathCalculator;
import main.java.com.hit.dm.*;

import java.util.List;
import java.util.stream.Collectors;

public class DeliveryService {
    private PathCalculator pathCalculator;

    public DeliveryService(IAlgoBestPathsCalculator algorithm) {
        this.pathCalculator = new PathCalculator(algorithm);
    }

    public DeliveryRoute calculateDeliveryRoute(MapGraph graph, Node start, Node destination, List<Node> deliveryPoints) {
        List<Node> shortestPath = pathCalculator.findShortestPath(graph, start, destination);
        List<Node> validStops = deliveryPoints.stream()
                .filter(shortestPath::contains)
                .collect(Collectors.toList());
        return new DeliveryRoute(shortestPath, validStops);
    }
}
