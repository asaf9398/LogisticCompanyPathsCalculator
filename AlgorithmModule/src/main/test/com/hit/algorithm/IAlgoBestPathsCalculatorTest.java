package main.test.com.hit.algorithm;

import main.java.com.hit.algorithm.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class IAlgoBestPathsCalculatorTest {

    @Test
    public void testDijkstraImplementation() {
        MapGraph graph = createSampleGraph();
        IAlgoBestPathsCalculator dijkstra = new DijkstraPathsCalcImpl();

        List<Node> result = dijkstra.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
    }

    @Test
    public void testBellmanFordImplementation() {
        MapGraph graph = createSampleGraph();
        IAlgoBestPathsCalculator bellmanFord = new BellmanFordPathsCalcImpl();

        List<Node> result = bellmanFord.calculateShortestPaths(graph, graph.getNodeList().get(0));
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
        assertEquals("C", result.get(2).getId());
    }


    @Test
    public void testAllAlgorithmsReturnSameResult() {
        MapGraph graph = createSampleGraph();

        IAlgoBestPathsCalculator dijkstra = new DijkstraPathsCalcImpl();
        IAlgoBestPathsCalculator bellmanFord = new BellmanFordPathsCalcImpl();

        List<Node> pathDijkstra = dijkstra.calculateShortestPaths(graph, graph.getNodeList().get(0));
        List<Node> pathBellmanFord = bellmanFord.calculateShortestPaths(graph, graph.getNodeList().get(0));

        assertEquals(pathDijkstra, pathBellmanFord);
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
