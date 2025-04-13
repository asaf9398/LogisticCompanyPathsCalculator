package ui;

import client.ClientRequestHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import models.MapGraph;
import models.Node;
import java.util.List;

public class RemovePointController {
    @FXML
    private ComboBox<String> comboPoints;

    private ClientRequestHandler requestHandler;
    private String graphName;

    public void setData(ClientRequestHandler requestHandler, String graphName, List<Node> nodes) {
        this.requestHandler = requestHandler;
        this.graphName = graphName;
        for (Node node : nodes) {
            comboPoints.getItems().add(node.getId());
        }
    }

    @FXML
    private void handleRemovePoint() {
        String selectedPoint = comboPoints.getValue();
        if (selectedPoint != null) {
            List<Node> nodeLst=requestHandler.getMap(graphName).getNodeList();
            Node wantedNode=null;
            for (Node node:nodeLst) {
                if (node.id.equals(selectedPoint))
                {
                    wantedNode=node;
                }
            }

            requestHandler.deleteNode(graphName, wantedNode);
            ((Stage) comboPoints.getScene().getWindow()).close();
        }
    }
}
