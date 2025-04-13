package main.test.com.hit.algorithm;

import main.java.com.hit.algorithm.*;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class BellmanFordPathsCalcImplTest {

    @Test
    public void testCalculateShortestPaths() {
        MapGraph graph = createSampleGraph();
        IAlgoBestPathsCalculator bellmanFord = new BellmanFordPathsCalcImpl();

        List<Node> result = bellmanFord.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
    }

    @Test
    public void testGraphWithCycles() {
        MapGraph graph = createGraphWithCycles();
        IAlgoBestPathsCalculator bellmanFord = new BellmanFordPathsCalcImpl();

        List<Node> result = bellmanFord.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(4, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
        assertEquals("D", result.get(3).getId());
    }

    @Test
    public void testDisconnectedGraph() {
        MapGraph graph = createDisconnectedGraph();
        IAlgoBestPathsCalculator bellmanFord = new BellmanFordPathsCalcImpl();

        List<Node> result = bellmanFord.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
    }

    @Test
    public void testGraphWithNegativeWeights() {
        MapGraph graph = createGraphWithNegativeWeights();
        IAlgoBestPathsCalculator bellmanFord = new BellmanFordPathsCalcImpl();

        List<Node> result = bellmanFord.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
    }

    private MapGraph createGraphWithNegativeWeights() {
        MapGraph graph = new MapGraph();
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);

        A.addEdge(B, -1);
        B.addEdge(C, -2);

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
        C.addEdge(D, 4);

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
