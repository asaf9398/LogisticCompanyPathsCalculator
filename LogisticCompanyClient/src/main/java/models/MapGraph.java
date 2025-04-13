package models;

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

    public String getGraphId()
    {
        return graphId;
    }

    public List<Node> getNodeList()
    {
        return nodeList;
    }

    public Node findNodeById(String key) {
        for (Node node:nodeList) {
        if (key.equals(node.getId())) {
            return node;
        }
        }
        return null;
    }

    public String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(String selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }
}
