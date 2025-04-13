package ui;

import client.ClientRequestHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import models.MapGraph;
import models.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoveEdgeController {
    @FXML
    private ComboBox<String> comboEdges;

    private ClientRequestHandler requestHandler;
    private String graphName;

    public void setData(ClientRequestHandler requestHandler, String graphName, MapGraph mapGraph) {
        this.requestHandler = requestHandler;
        this.graphName = graphName;


        List<String> edges = new ArrayList<>();
        for (Node node : mapGraph.getNodeList()) {
            for (Map.Entry<String, Double> edge : node.getEdgesList().entrySet()) {
                edges.add(node.getId() + " -> " + edge.getKey());
            }
        }
        comboEdges.getItems().addAll(edges);
    }

    @FXML
    private void handleRemoveEdge() {
        String selectedEdge = comboEdges.getValue();
        if (selectedEdge == null) {
            return;
        }


        String[] edgeParts = selectedEdge.split(" -> ");
        String source = edgeParts[0];
        String destination = edgeParts[1];

        requestHandler.deleteEdge(graphName, source, destination);
        ((Stage) comboEdges.getScene().getWindow()).close();
    }
}
