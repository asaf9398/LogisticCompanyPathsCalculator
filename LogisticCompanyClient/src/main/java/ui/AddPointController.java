package ui;

import client.ClientRequestHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Node;

public class AddPointController {
    @FXML
    private TextField idField, latField, lonField;

    private ClientRequestHandler requestHandler;
    private String graphName;

    public void setData(ClientRequestHandler requestHandler, String graphName) {
        this.requestHandler = requestHandler;
        this.graphName = graphName;
    }

    @FXML
    private void handleAddPoint() {
        String id = idField.getText();
        double lat = Double.parseDouble(latField.getText());
        double lon = Double.parseDouble(lonField.getText());

        Node node = new Node(id, id, lat, lon);
        requestHandler.addNode(graphName, node);

        ((Stage) idField.getScene().getWindow()).close();
    }

    public void setInitialCoordinates(double latitude, double longitude) {
        latField.setText(String.valueOf(latitude));
        lonField.setText(String.valueOf(longitude));
    }

}
