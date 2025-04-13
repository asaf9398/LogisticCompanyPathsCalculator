package main.test.com.hit.algorithm;

import main.java.com.hit.algorithm.*;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class DijkstraPathsCalcImplTest {

    @Test
    public void testCalculateShortestPaths() {
        MapGraph graph = createSampleGraph();
        IAlgoBestPathsCalculator dijkstra = new DijkstraPathsCalcImpl();

        List<Node> result = dijkstra.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
    }

    @Test
    public void testGraphWithDifferentWeights() {
        MapGraph graph = createGraphWithDifferentWeights();
        IAlgoBestPathsCalculator dijkstra = new DijkstraPathsCalcImpl();
        List<Node> result = dijkstra.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(4, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
        assertEquals("D", result.get(3).getId());
    }

    @Test
    public void testDisconnectedGraph() {
        MapGraph graph = createDisconnectedGraph();
        IAlgoBestPathsCalculator dijkstra = new DijkstraPathsCalcImpl();
        List<Node> result = dijkstra.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
    }

    @Test
    public void testCalculateDistanceMatrix() {
        MapGraph graph = createSampleGraph();
        IAlgoBestPathsCalculator dijkstra = new DijkstraPathsCalcImpl();
        double[][] distanceMatrix = dijkstra.calculateDistanceMatrix(graph);

        assertEquals(0.0, distanceMatrix[0][0], 0.001);
        assertEquals(1.0, distanceMatrix[0][1], 0.001);
        assertEquals(3.0, distanceMatrix[0][2], 0.001);
        assertEquals(Double.POSITIVE_INFINITY, distanceMatrix[2][1], 0.001);
    }

    @Test
    public void testGraphWithCycles() {
        MapGraph graph = createGraphWithCycles();
        IAlgoBestPathsCalculator dijkstra = new DijkstraPathsCalcImpl();
        List<Node> result = dijkstra.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(4, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
        assertEquals("D", result.get(3).getId());
    }

    private MapGraph createGraphWithCycles() {
        MapGraph graph = new MapGraph();
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);

        A.addEdge(B, 1);
        B.addEdge(C, 2);
        C.addEdge(A, 3);
        C.addEdge(D, 1);

        return graph;
    }

    private MapGraph createDisconnectedGraph() {
        MapGraph graph = new MapGraph();
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);

        A.addEdge(B, 1);
        B.addEdge(C, 2);

        return graph;
    }

    private MapGraph createGraphWithDifferentWeights() {
        MapGraph graph = new MapGraph();
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);

        A.addEdge(B, 1);
        A.addEdge(C, 5);
        B.addEdge(C, 2);
        B.addEdge(D, 4);
        C.addEdge(D, 1);

        return graph;
    }

    private MapGraph createSampleGraph() {
        MapGraph graph = new MapGraph();
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);

        A.addEdge(B, 1);
        B.addEdge(C, 2);

        return graph;
    }
}
