package ui;

import client.ClientRequestHandler;
import com.sothawo.mapjfx.Coordinate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Node;

import java.util.List;

public class AddEdgeController {
    @FXML
    private ComboBox<String> comboSource, comboDest;
    @FXML
    private TextField weightField;
    @FXML
    private CheckBox autoDistanceCheckBox;

    private ClientRequestHandler requestHandler;
    private String graphName;
    private List<Node> nodes;

    public void setData(ClientRequestHandler requestHandler, String graphName, List<Node> nodes) {
        this.requestHandler = requestHandler;
        this.graphName = graphName;
        this.nodes = nodes;

        for (Node node : nodes) {
            comboSource.getItems().add(node.getId());
            comboDest.getItems().add(node.getId());
        }

        comboSource.setOnAction(e -> updateDistanceIfNeeded());
        comboDest.setOnAction(e -> updateDistanceIfNeeded());
    }

    @FXML
    private void toggleDistanceCalculation() {
        boolean isChecked = autoDistanceCheckBox.isSelected();
        weightField.setDisable(isChecked); //

        if (isChecked) {
            updateDistanceIfNeeded();
        }
    }

    private void updateDistanceIfNeeded() {
        if (!autoDistanceCheckBox.isSelected()) return;

        String sourceId = comboSource.getValue();
        String destId = comboDest.getValue();

        if (sourceId == null || destId == null || sourceId.equals(destId)) return;

        Node sourceNode = findNodeById(sourceId);
        Node destNode = findNodeById(destId);

        if (sourceNode != null && destNode != null) {
            double distance = calculateDistance(
                    new Coordinate(sourceNode.getLatitude(), sourceNode.getLongitude()),
                    new Coordinate(destNode.getLatitude(), destNode.getLongitude())
            );

            weightField.setText(String.format("%.2f", distance));
        }
    }

    @FXML
    private void handleAddEdge() {
        String source = comboSource.getValue();
        String dest = comboDest.getValue();
        if (source == null || dest == null || source.equals(dest)) {
            System.out.println("Error: Invalid source or destination.");
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(weightField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid weight input.");
            return;
        }

        requestHandler.addEdge(graphName, source, dest, weight);
        ((Stage) comboSource.getScene().getWindow()).close();
    }

    private Node findNodeById(String nodeId) {
        return nodes.stream().filter(n -> n.getId().equals(nodeId)).findFirst().orElse(null);
    }

    public static double calculateDistance(Coordinate coord1, Coordinate coord2) {
        double EARTH_RADIUS = 6371.0;
        double lat1 = Math.toRadians(coord1.getLatitude());
        double lon1 = Math.toRadians(coord1.getLongitude());
        double lat2 = Math.toRadians(coord2.getLatitude());
        double lon2 = Math.toRadians(coord2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
