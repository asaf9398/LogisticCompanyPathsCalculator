package main.java.com.hit.dao;

import main.java.com.hit.algorithm.MapGraph;
import main.java.com.hit.algorithm.Node;

public class GraphDao {
    private MapGraph graph = new MapGraph();

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
