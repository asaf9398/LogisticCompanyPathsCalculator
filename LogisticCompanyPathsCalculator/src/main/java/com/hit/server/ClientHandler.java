package main.java.com.hit.server;

import main.java.com.hit.algorithm.*;
import main.java.com.hit.dm.*;
import main.java.com.hit.services.DeliveryService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private IAlgoBestPathsCalculator algo;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gson;

    private List<MapGraph> graphList;
    private DeliveryService deliveryService;

    public ClientHandler(Socket socket, IAlgoBestPathsCalculator algo) {
        this.clientSocket = socket;
        this.algo = algo;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            this.graphList = DBManager.loadGraphsFromFile();
            this.deliveryService = new DeliveryService(algo);

            String jsonRequest = input.readLine();
            if (jsonRequest == null) {
                return;
            }
            Request request = gson.fromJson(jsonRequest, Request.class);
            Response response = processRequest(request);

            String jsonResponse = gson.toJson(response);
            output.println(jsonResponse);

            DBManager.saveGraphsToFile(this.graphList);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) clientSocket.close();
                if (input != null) input.close();
                if (output != null) output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Response processRequest(Request request) {
        switch (request.getAction()) {
            case "getNodes":
                return handleGetNodes(request.getPayload());

            case "addNode":
                return handleAddNode(request.getPayload());

            case "deleteNode":
                return handleDeleteNode(request.getPayload());

            case "addEdge":
                return handleAddEdge(request.getPayload());

            case "deleteEdge":
                return handleDeleteEdge(request.getPayload());

            case "calculateRoute":
                return handleCalculateRoute(request.getPayload());

            case "getGraphs":
                return handleGetGraphs();

            case "addMap":
                return handleAddMap(request.getPayload());

            case "deleteMap":
                return handleDeleteMap(request.getPayload());

            case "getMap":
                return handleGetMap(request.getPayload());

            case "setAlgorithm":
                return handleSetAlgorithm(request);

            default:
                return new Response(false, "Unknown action: " + request.getAction());
        }
    }

    private Response handleSetAlgorithm(Request request) {
        Map<String, String> data = gson.fromJson(request.getPayload(), Map.class);
        String graphName = data.get("graphName");
        String algorithm = data.get("algorithm");
        MapGraph graph = null;

        for (MapGraph currGraph:graphList) {
            if (currGraph.getGraphId().equals(graphName))
            {
                graph=currGraph;
            }
        }

        switch (algorithm){
            case "Dijkstra":
                deliveryService = new DeliveryService(new DijkstraPathsCalcImpl());
                break;
            case "Bellman-Ford":
                deliveryService = new DeliveryService(new BellmanFordPathsCalcImpl());
                break;
            default:
                return new Response(false, "Algorithm not found: " + algorithm);
        }

        if (graph == null) {
            return new Response(false, "Graph not found: " + graphName);
        }

        graph.setSelectedAlgorithm(algorithm);
        return new Response(true, "Algorithm set to " + algorithm + " for graph " + graphName);
    }

    private Response handleDeleteEdge(String payload) {
        EdgeRequest req = gson.fromJson(payload, EdgeRequest.class);
        MapGraph graph = findGraphByName(req.getGraphName());

        if (graph == null) {
            return new Response(false, "Graph not found: " + req.getGraphName());
        }

        graph.deleteEdge(req.sourceId, req.destId);
        return new Response(true, "Edge deleted: " + "("+req.sourceId+","+req.destId+")");
    }


    private Response handleDeleteNode(String payload) {
        NodeRequest req = gson.fromJson(payload, NodeRequest.class);
        MapGraph graph = findGraphByName(req.getGraphName());

        if (graph == null) {
            return new Response(false, "Graph not found: " + req.getGraphName());
        }

        Node node = new Node(req.node.getId(), req.node.getName(), req.node.getLatitude(), req.node.getLongitude());
        graph.removeNode(node);
        for (Node otherNodeInGraph:graph.getNodeList()) {
            if(otherNodeInGraph.edgesList.containsKey(node.id))
            {
                otherNodeInGraph.edgesList.remove(node.id);
            }
        }
        return new Response(true, "Node added: " + node.getName());
    }

    private Response handleGetMap(String payload) {
        MapGraph graph = findGraphByName(payload);

        if (graph == null) {
            return new Response(false, "Graph not found: " + payload);
        }

        return new Response(true, gson.toJson(graph));
    }


    private Response handleDeleteMap(String payload) {
        MapGraph graph = findGraphByName(payload);

        if (graph == null) {
            return new Response(false, "Map not found: " + payload);
        }

        graphList.remove(graph);

        DBManager.saveGraphsToFile(graphList);

        return new Response(true, "Map deleted: " + payload);
    }


    private Response handleAddMap(String payload) {

        if (findGraphByName(payload)!=null) {
            return new Response(false, "Map already exists: " + payload);
        }

        MapGraph newGraph = new MapGraph(payload);
        graphList.add(newGraph);

        DBManager.saveGraphsToFile(graphList);

        return new Response(true, "Map added: " + payload);
    }


    private Response handleGetGraphs() {
        List<String> graphNames =  new ArrayList<>();

        for (MapGraph map:graphList) {
        graphNames.add(map.getGraphId());
        }

        return new Response(true, gson.toJson(graphNames));
    }


    private Response handleGetNodes(String payload) {
        MapGraph graph = findGraphByName(payload);

        if (graph == null) {
            return new Response(false, "Graph not found: " + payload);
        }

        List<Node> nodes = graph.getNodeList();
        return new Response(true, gson.toJson(nodes));
    }

    private Response handleAddNode(String payload) {
        NodeRequest req = gson.fromJson(payload, NodeRequest.class);
        MapGraph graph = findGraphByName(req.getGraphName());

        if (graph == null) {
            return new Response(false, "Graph not found: " + req.getGraphName());
        }

        Node node = new Node(req.node.getId(), req.node.getName(), req.node.getLatitude(), req.node.getLongitude());
        graph.addNode(node);
        return new Response(true, "Node added: " + node.getName());
    }

    private Response handleAddEdge(String payload) {
        EdgeRequest req = gson.fromJson(payload, EdgeRequest.class);
        MapGraph graph = findGraphByName(req.getGraphName());

        if (graph == null) {
            return new Response(false, "Graph not found: " + req.getGraphName());
        }

        graph.addEdge(req.getSourceId(), req.getDestId(), req.getWeight());
        return new Response(true, "Edge added: " + req.getSourceId() + " → " + req.getDestId());
    }

    private Response handleCalculateRoute(String payload) {
        CalcRequest req = gson.fromJson(payload, CalcRequest.class);
        MapGraph graph = findGraphByName(req.getGraphName());

        if (graph == null) {
            return new Response(false, "Graph not found: " + req.getGraphName());
        }

        Node start = graph.findNodeById(req.getStart());
        Node dest = graph.findNodeById(req.getDestination());

        if (start == null || dest == null) {
            return new Response(false, "Start or Destination not found in graph");
        }

        var route = deliveryService.calculateDeliveryRoute(graph, start, dest, List.of());
        return new Response(true, gson.toJson(route));
    }

    private MapGraph findGraphByName(String name) {
        Optional<MapGraph> graph = graphList.stream()
                .filter(g -> g.getGraphId().equals(name))
                .findFirst();
        return graph.orElse(null);
    }

    public class GraphRequest {
        private String graphId;
        public String getGraphId() { return graphId; }
    }

    public class NodeRequest {
        private String graphName;
        private Node node;
        public String getGraphName() { return graphName; }
        public Node getNode() { return node; }
        public NodeRequest(){}
    }

    public class EdgeRequest {
        private String graphName;
        private String sourceId;
        private String destId;
        private double weight;
        public String getGraphName() { return graphName; }
        public String getSourceId() { return sourceId; }
        public String getDestId() { return destId; }
        public double getWeight() { return weight; }
    }

    public class CalcRequest {
        private String graphName;
        private String start;
        private String destination;
        public String getGraphName() { return graphName; }
        public String getStart() { return start; }
        public String getDestination() { return destination; }
    }
}
