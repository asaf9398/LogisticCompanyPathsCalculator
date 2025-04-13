package main.java.com.hit.services;

import main.java.com.hit.algorithm.MapGraph;
import main.java.com.hit.algorithm.Node;
import main.java.com.hit.dm.*;

public class GraphService {
    private MapGraph graph;

    public GraphService() {
        this.graph = new MapGraph();
    }

    public void addNode(Node node) {
        graph.addNode(node);
    }

    public void addEdge(Node source, Node destination, double weight) {
        graph.addEdge(source.getId(), destination.getId(), weight);
    }

    public MapGraph getGraph() {
        return graph;
    }
}

