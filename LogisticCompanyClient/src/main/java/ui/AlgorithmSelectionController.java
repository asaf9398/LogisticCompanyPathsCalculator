package ui;

import client.ClientRequestHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AlgorithmSelectionController {

    @FXML
    private ComboBox<String> algorithmComboBox;
    @FXML
    private Button applyButton;

    private ClientRequestHandler requestHandler;
    private String graphName;

    public void setData(ClientRequestHandler requestHandler, String graphName) {
        this.requestHandler = requestHandler;
        this.graphName = graphName;


        algorithmComboBox.getItems().addAll("Dijkstra", "Bellman-Ford");
        algorithmComboBox.setValue("Dijkstra");
    }

    @FXML
    private void applyAlgorithmSelection() {
        String selectedAlgorithm = algorithmComboBox.getValue();
        System.out.println("Selected Algorithm: " + selectedAlgorithm);

        requestHandler.setAlgorithm(graphName, selectedAlgorithm);

        ((Stage) applyButton.getScene().getWindow()).close();
    }
}
