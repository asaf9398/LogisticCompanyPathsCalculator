package ui;

import client.ClientRequestHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import networking.Response;

import java.util.List;

public class MapManagementController {
    @FXML
    private TextField mapNameField;
    @FXML
    private ComboBox<String> comboMaps;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;

    private ClientRequestHandler requestHandler;

    public void initialize() {
        requestHandler = new ClientRequestHandler("localhost", 5000);
        loadMaps();
    }

    private void loadMaps() {
        List<String> maps = requestHandler.getGraphs();

        if (maps == null || maps.isEmpty()) {
            System.out.println("No maps found.");
            return;
        }

        comboMaps.getItems().clear();
        comboMaps.getItems().addAll(maps);

        System.out.println("Loaded " + maps.size() + " maps.");
    }


    @FXML
    private void addMap() {
        String mapName = mapNameField.getText().trim();

        if (mapName.isEmpty()) {
            System.out.println("Error: Map name cannot be empty.");
            return;
        }

        Response response = requestHandler.addMap(mapName);

        if (response!=null)
        {
            System.out.println("Server response: " + response.message);
        }
        loadMaps();
    }


    @FXML
    private void deleteMap() {
        String selectedMap = comboMaps.getValue();

        if (selectedMap == null || selectedMap.isEmpty()) {
            System.out.println("Error: No map selected.");
            return;
        }

        Response response = requestHandler.deleteMap(selectedMap);

        if (response!=null)
        {
            System.out.println("Server response: " + response.message);
        }

        loadMaps();
    }
}
