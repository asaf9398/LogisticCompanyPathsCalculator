package main.java.com.hit.algorithm;


import java.util.List;
import java.util.Map;

public interface IAlgoBestPathsCalculator {
    List<Node> calculateShortestPaths(MapGraph graph, Node source);
    double[][] calculateDistanceMatrix(MapGraph graph);
    List<Node> getShortestPath(Node destination, Map<Node, Node> predecessors);
    }
