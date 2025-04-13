package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    @FXML
    private void openMapSelection() {
        loadScene("/fxml/map_selection.fxml", "Map Selection");
    }

    @FXML
    private void openMapView() {
        loadScene("/fxml/map_view.fxml", "Map View");
    }

    @FXML
    private void openPointManagement() {
        loadScene("/fxml/point_management.fxml", "Point Management");
    }

    @FXML
    private void openMapManagement() {
        loadScene("/fxml/map_management.fxml", "Map Management");
    }


    private void loadScene(String resource, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
