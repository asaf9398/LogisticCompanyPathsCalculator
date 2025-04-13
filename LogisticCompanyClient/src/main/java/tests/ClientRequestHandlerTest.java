package tests;

import client.Client;
import client.ClientRequestHandler;
import com.google.gson.Gson;
import models.DeliveryRoute;
import models.MapGraph;
import models.Node;
import networking.Request;
import networking.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ClientRequestHandlerTest {
    private Client mockClient;
    private ClientRequestHandler clientRequestHandler;
    private Gson gson;

    @Before
    public void setUp() {
        mockClient = Mockito.mock(Client.class);
        clientRequestHandler = Mockito.spy(new ClientRequestHandler("localhost", 8080));
        gson = new Gson();

        try {
            java.lang.reflect.Field clientField = ClientRequestHandler.class.getDeclaredField("client");
            clientField.setAccessible(true);
            clientField.set(clientRequestHandler, mockClient);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to inject mock client", e);
        }
    }

    @Test
    public void testGetMap_Success() throws Exception {
        String mapName = "testMap";
        MapGraph mockMapGraph = new MapGraph();
        Response mockResponse = new Response(true, gson.toJson(mockMapGraph));

        Mockito.when(mockClient.sendRequest(Mockito.any(Request.class))).thenReturn(mockResponse);

        MapGraph result = clientRequestHandler.getMap(mapName);

        assertNotNull(result);
        Mockito.verify(mockClient, times(1)).sendRequest(Mockito.any(Request.class));
    }

    @Test
    public void testGetMap_Failure() throws Exception {
        Mockito.when(mockClient.sendRequest(Mockito.any(Request.class))).thenReturn(new Response(false, "Error"));

        MapGraph result = clientRequestHandler.getMap("invalidMap");

        assertNull(result);
    }

    @Test
    public void testGetGraphs_Success() throws Exception {
        List<String> graphs = List.of("Graph1", "Graph2");
        Response mockResponse = new Response(true, gson.toJson(graphs));

        Mockito.when(mockClient.sendRequest(Mockito.any(Request.class))).thenReturn(mockResponse);

        List<String> result = clientRequestHandler.getGraphs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Graph1", result.get(0));
    }

    @Test
    public void testAddNode_Success() throws Exception {
        Node node = new Node("C");
        Response mockResponse = new Response(true, "Node added");

        Mockito.when(mockClient.sendRequest(Mockito.any(Request.class))).thenReturn(mockResponse);

        String result = clientRequestHandler.addNode("testGraph", node);

        assertEquals("Node added", result);
    }

    @Test
    public void testCalculateRoute_Success() throws Exception {
        List<Node> path = List.of(new Node("A"), new Node("B"));
        DeliveryRoute mockRoute = new DeliveryRoute(path);

        Response mockResponse = new Response(true, gson.toJson(mockRoute));

        Mockito.when(mockClient.sendRequest(Mockito.any(Request.class))).thenReturn(mockResponse);

        DeliveryRoute result = clientRequestHandler.calculateRoute("testGraph", "A", "B");

        assertNotNull(result);
        assertEquals(2, result.getPath().size());
        assertEquals("A", result.getPath().get(0).getName());
        assertEquals("B", result.getPath().get(1).getName());
    }

    @Test
    public void testSetAlgorithm_Success() throws Exception {
        clientRequestHandler.setAlgorithm("testGraph", "Dijkstra");

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        Mockito.verify(mockClient, times(1)).sendRequest(requestCaptor.capture());

        Request sentRequest = requestCaptor.getValue();
        assertEquals("setAlgorithm", sentRequest.getAction());
        assertTrue(sentRequest.getPayload().contains("Dijkstra"));
    }

    @Test
    public void testAddEdge_Success() throws Exception {
        Mockito.when(mockClient.sendRequest(Mockito.any(Request.class))).thenReturn(new Response(true, "Edge added"));

        Response result = clientRequestHandler.addEdge("testGraph", "A", "B", 10.5);

        assertTrue(result.isSuccess());
        assertEquals("Edge added", result.getMessage());
    }

    @Test
    public void testDeleteEdge_Success() throws Exception {
        Mockito.when(mockClient.sendRequest(Mockito.any(Request.class))).thenReturn(new Response(true, "Edge deleted"));

        Response result = clientRequestHandler.deleteEdge("testGraph", "A", "B");

        assertTrue(result.isSuccess());
        assertEquals("Edge deleted", result.getMessage());
    }

    @Test
    public void testDeleteNode_Success() throws Exception {
        Node node = new Node("C");
        Mockito.when(mockClient.sendRequest(Mockito.any(Request.class))).thenReturn(new Response(true, "Node deleted"));

        Response result = clientRequestHandler.deleteNode("testGraph", node);

        assertTrue(result.isSuccess());
        assertEquals("Node deleted", result.getMessage());
    }
}
