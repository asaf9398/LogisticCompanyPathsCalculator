package main.java.com.hit.algorithm;

import java.util.ArrayList;
import java.util.List;

public class MapGraph {
    private String graphId;
    private List<Node> nodeList;
    private String selectedAlgorithm = "Dijkstra";

    public MapGraph() {
        this.graphId = "Default";
        this.nodeList = new ArrayList<>();
    }
    public MapGraph(String graphId) {
        this.graphId = graphId;
        this.nodeList = new ArrayList<>();
    }

    public String getGraphId() {
        return graphId;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(String selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public void addNode(Node node) {
        if (!nodeList.contains(node)) {
            nodeList.add(node);
        }
    }

    public void removeNode(Node node) {
        if (nodeList.contains(node)) {
            nodeList.remove(node);
        }
    }

    public void addEdge(String sourceId, String destId, double weight) {
        Node source = findNodeById(sourceId);
        Node destination = findNodeById(destId);

        if (source == null || destination == null) {
            System.out.println("❌ Error: One of the nodes does not exist.");
            return;
        }

        source.addEdge(destination, weight);
        destination.addEdge(source, weight);
    }

    public void deleteEdge(String sourceId, String destId) {
        Node source = findNodeById(sourceId);
        Node destination = findNodeById(destId);

        if (source == null || destination == null) {
            System.out.println("❌ Error: One of the nodes does not exist.");
            return;
        }

        source.removeEdge(destId);
    }


    public Node findNodeById(String id) {
        for (Node node : nodeList) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }
}
