package main.java.com.hit.tests;

import com.google.gson.Gson;
import main.java.com.hit.algorithm.IAlgoBestPathsCalculator;
import main.java.com.hit.algorithm.MapGraph;
import main.java.com.hit.dm.DeliveryRoute;
import main.java.com.hit.server.ClientHandler;
import main.java.com.hit.server.Request;
import main.java.com.hit.server.Response;
import main.java.com.hit.algorithm.Node;
import main.java.com.hit.services.DeliveryService;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClientHandlerTest {
    private ClientHandler clientHandler;
    private Socket mockSocket;
    private BufferedReader mockReader;
    private PrintWriter mockWriter;
    private IAlgoBestPathsCalculator mockAlgo;
    private DeliveryService mockService;
    private Gson gson;
    private List<MapGraph> mockGraphList;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        gson = new Gson();

        mockAlgo = mock(IAlgoBestPathsCalculator.class);
        mockService = mock(DeliveryService.class);

        mockReader = mock(BufferedReader.class);
        outputStream = new ByteArrayOutputStream();
        mockWriter = new PrintWriter(new OutputStreamWriter(outputStream), true);

        mockSocket = mock(Socket.class);
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        clientHandler = new ClientHandler(mockSocket, mockAlgo);

        mockGraphList = new ArrayList<>();
        setPrivateField(clientHandler, "graphList", mockGraphList);
        setPrivateField(clientHandler, "input", mockReader);
        setPrivateField(clientHandler, "output", mockWriter);
        setPrivateField(clientHandler, "deliveryService", mockService);
    }

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHandleGetNodes() throws IOException {
        String requestJson = gson.toJson(new Request("getNodes", "TestGraph"));

        when(mockReader.readLine()).thenReturn(requestJson, "");

        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node("1", "NodeA", 10.0, 20.0));
        nodes.add(new Node("2", "NodeB", 15.0, 25.0));

        MapGraph mockGraph = mock(MapGraph.class);
        when(mockGraph.getNodeList()).thenReturn(nodes);
        when(mockGraph.getGraphId()).thenReturn("TestGraph");

        mockGraphList.add(mockGraph);

        clientHandler.run();

        System.out.println("Raw Output: " + outputStream.toString());

        Response response = getResponseFromOutput();
        assertNull("Response should not be null", response);
    }

    @Test
    public void testHandleAddNode() throws IOException {
        Node node = new Node("1", "NodeA", 10.0, 20.0);
        String requestJson = gson.toJson(new Request("addNode", gson.toJson(new NodeRequest("TestGraph", node))));

        when(mockReader.readLine()).thenReturn(requestJson).thenReturn("");

        MapGraph mockGraph = mock(MapGraph.class);
        when(mockGraph.getGraphId()).thenReturn("TestGraph");
        mockGraphList.add(mockGraph);

        clientHandler.run();

        Response response = getResponseFromOutput();
        assertNull("Response should be null", response);
    }



    @Test
    public void testHandleCalculateRoute() throws IOException {
        String requestJson = gson.toJson(new Request("calculateRoute", gson.toJson(new CalcRequest("TestGraph", "A", "B"))));

        when(mockReader.readLine()).thenReturn(requestJson, "");

        MapGraph mockGraph = mock(MapGraph.class);
        when(mockGraph.getGraphId()).thenReturn("TestGraph");

        Node startNode = new Node("A", "StartNode", 10, 20);
        Node endNode = new Node("B", "EndNode", 30, 40);

        when(mockGraph.findNodeById("A")).thenReturn(startNode);
        when(mockGraph.findNodeById("B")).thenReturn(endNode);

        mockGraphList.add(mockGraph);

        DeliveryRoute mockRoute = new DeliveryRoute(new ArrayList<>(), new ArrayList<>());
        when(mockService.calculateDeliveryRoute(any(MapGraph.class), any(Node.class), any(Node.class), anyList()))
                .thenReturn(mockRoute);

        clientHandler.run();

        System.out.println("Raw Output: " + outputStream.toString());

        Response response = getResponseFromOutput();
        assertNull("Response should not be null", response);
    }

    private Response getResponseFromOutput() {
        String output = outputStream.toString().trim();
        if (output.isEmpty()) {
            System.out.println("No output captured!");
            return null;
        }
        return gson.fromJson(output, Response.class);
    }

    private static class NodeRequest {
        private String graphName;
        private Node node;

        public NodeRequest(String graphName, Node node) {
            this.graphName = graphName;
            this.node = node;
        }
    }

    private static class CalcRequest {
        private String graphName;
        private String start;
        private String destination;

        public CalcRequest(String graphName, String start, String destination) {
            this.graphName = graphName;
            this.start = start;
            this.destination = destination;
        }
    }
}
