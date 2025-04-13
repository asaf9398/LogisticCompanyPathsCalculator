package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.DeliveryRoute;
import models.MapGraph;
import models.Node;
import networking.Request;
import networking.Response;

import java.util.List;
import java.util.Map;

public class ClientRequestHandler {
    public  Client client;
    private Gson gson;

    public ClientRequestHandler(String serverAddress, int serverPort) {
        this.client = new Client(serverAddress, serverPort);
        this.gson = new Gson();
    }


    public MapGraph getMap(String mapName) {
        try {
            client.connect();
            Response response = client.sendRequest(new Request("getMap", mapName));
            client.close();

            if (response.isSuccess()) {
                return gson.fromJson(response.message, MapGraph.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<String> getGraphs() {
        try {
            client.connect();
            Response resp = client.sendRequest(new Request("getGraphs", ""));
            client.close();
            if (!resp.isSuccess()) {
                System.out.println("Error: " + resp.getMessage());
                return List.of();
            }
            return gson.fromJson(resp.getMessage(), new TypeToken<List<String>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Response addMap(String mapName) {
        Response resp = null;
        try {
            client.connect();
            resp= client.sendRequest(new Request("addMap", mapName));
            client.close();
            return resp;
        }
        catch (Exception e)
        {
            return resp;
        }
    }

    public Response deleteMap(String mapName)
    {
     Response resp = null;
        try {
        client.connect();
        resp= client.sendRequest(new Request("deleteMap", mapName));
        client.close();
        return resp;
    }
        catch (Exception e)
    {
        return resp;
    }
}


    public List<Node> getNodes(String graphName) {
        try {
            client.connect();
            Response resp = client.sendRequest(new Request("getNodes", graphName));
            client.close();
            if (!resp.isSuccess()) {
                System.out.println("Error: " + resp.getMessage());
                return List.of();
            }
            return gson.fromJson(resp.getMessage(), new TypeToken<List<Node>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    public String addNode(String graphName, Node node) {
        try {
            client.connect();
            NodeRequest nodeRequest=new NodeRequest(graphName, node);
            String payload = gson.toJson(nodeRequest);
            Response resp = client.sendRequest(new Request("addNode", payload));
            client.close();
            return resp.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding node: " + e.getMessage();
        }
    }


    public DeliveryRoute calculateRoute(String graphName, String startId, String destId) {
        try {
            client.connect();
            String payload = gson.toJson(new RouteRequest(graphName, startId, destId));
            Response resp = client.sendRequest(new Request("calculateRoute", payload));
            client.close();
            return gson.fromJson(resp.getMessage(), DeliveryRoute.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setAlgorithm(String graphName, String algorithm) {
        try {
            client.connect();
            String payload = gson.toJson(Map.of("graphName", graphName, "algorithm", algorithm));
            client.sendRequest(new Request("setAlgorithm", payload));
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Response addEdge(String graphName, String sourceId, String destId, double weight) {
        try {
            client.connect();
            String payload = gson.toJson(new EdgeRequest(graphName, sourceId, destId, weight));
            Response resp = client.sendRequest(new Request("addEdge", payload));
            client.close();
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Error adding edge: " + e.getMessage());
        }
    }

    public Response deleteEdge(String graphName, String sourceId, String destId) {
        try {
            client.connect();
            String payload = gson.toJson(new EdgeRequest(graphName, sourceId, destId, 0.0));
            Response resp = client.sendRequest(new Request("deleteEdge", payload));
            client.close();
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Error deleting edge: " + e.getMessage());
        }
    }

    public Response deleteNode(String graphName, Node node) {
        try {
            client.connect();
            NodeRequest nodeRequest=new NodeRequest(graphName, node);
            String payload = gson.toJson(nodeRequest);
            Response resp = client.sendRequest(new Request("deleteNode", payload));
            client.close();
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Error deleting node: " + e.getMessage());
        }
    }

    private static class EdgeRequest {
        String graphName;
        String sourceId;
        String destId;
        double weight;

        public EdgeRequest(String graphName, String sourceId, String destId, double weight) {
            this.graphName = graphName;
            this.sourceId = sourceId;
            this.destId = destId;
            this.weight = weight;
        }
    }


    public static class NodeRequest {
        String graphName;
        Node node;
        public NodeRequest(String graphName, Node node) {
            this.graphName = graphName;
            this.node = node;
        }
    }

    private static class RouteRequest {
        String graphName;
        String start;
        String destination;
        public RouteRequest(String graphName, String start, String destination) {
            this.graphName = graphName;
            this.start = start;
            this.destination = destination;
        }
    }
}
