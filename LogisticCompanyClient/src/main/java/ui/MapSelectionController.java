package ui;

import client.ClientRequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import models.MapGraph;
import models.Node;

import java.io.IOException;
import java.util.List;

public class MapSelectionController {
    @FXML
    private ComboBox<String> comboBoxGraph;
    @FXML
    private Button loadButton;

    private ClientRequestHandler requestHandler;
    private String selectedGraph = null;

    public void initialize() {
        requestHandler = new ClientRequestHandler("localhost", 5000);
        loadGraphs();
    }

    private void loadGraphs() {
        List<String> graphs = requestHandler.getGraphs();
        comboBoxGraph.getItems().clear();
        comboBoxGraph.getItems().addAll(graphs);

        comboBoxGraph.setOnAction(e -> {
            selectedGraph = comboBoxGraph.getValue();
        });

        System.out.println("Loaded " + graphs.size() + " graphs.");
    }

    private void loadNodes() {
        if (selectedGraph == null) {
            System.out.println("No graph selected.");
            return;
        }

        List<Node> nodes = requestHandler.getNodes(selectedGraph);
        comboBoxGraph.getItems().clear();
        for (Node n : nodes) {
            comboBoxGraph.getItems().add(n.getId());
        }
        System.out.println("Loaded " + nodes.size() + " nodes from graph: " + selectedGraph);
    }

    @FXML
    private void loadSelectedMap() {
        String selectedMap = comboBoxGraph.getValue();
        if (selectedMap == null || selectedMap.isEmpty()) {
            System.out.println("No map selected.");
            return;
        }

        System.out.println("Loading map: " + selectedMap);

        MapGraph mapGraph = requestHandler.getMap(selectedMap);
        if (mapGraph == null) {
            System.out.println("Failed to load map.");
            return;
        }

        openMapView(mapGraph);
    }


    private void openMapView(MapGraph mapGraph) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/map_view.fxml"));
            Parent root = loader.load();


            MapViewController controller = loader.getController();
            controller.setMapGraph(mapGraph);

            Stage stage = new Stage();
            stage.setTitle("Map View: " + mapGraph.getGraphId());
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
