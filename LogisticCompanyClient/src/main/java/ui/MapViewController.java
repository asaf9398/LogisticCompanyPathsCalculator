package ui;

import client.ClientRequestHandler;
import com.sothawo.mapjfx.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.DeliveryRoute;
import models.MapGraph;
import models.Node;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MapViewController {
    @FXML
    private ComboBox<String> comboStart;
    @FXML
    private ComboBox<String> comboDest;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private Label totalDistanceLabel;
    @FXML
    private Label routePathLabel;
    @FXML
    private StackPane mapContainer;

    private MapView mapView;
    private MapGraph mapGraph;
    private List<Marker> markers = new ArrayList<>();
    private List<CoordinateLine> edgeLines = new ArrayList<>();
    private ClientRequestHandler requestHandler;


    public void initialize() {
        requestHandler = new ClientRequestHandler("localhost", 5000);
        mapView = new MapView();
        mapView.initialize(Configuration.builder().showZoomControls(true).build());

        Platform.runLater(() -> {
            mapContainer.getChildren().add(mapView);
            mapView.setCenter(new Coordinate(32.0853, 34.7818));
            mapView.setZoom(10);
        });

        mapView.setOnMouseClicked(this::handleMapDoubleClick);
    }

    private void handleMapDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) { //
            Coordinate coord = mapView.getCenter(); //
            showAddPointDialog(coord.getLatitude(), coord.getLongitude());
        }
    }

    private void showAddPointDialog(double latitude, double longitude) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add New Point");
            alert.setHeaderText("Coordinates Selected:");
            alert.setContentText("Latitude: " + latitude + "\nLongitude: " + longitude);

            ButtonType addPointButton = new ButtonType("Add New Point");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());

            alert.getButtonTypes().setAll(addPointButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == addPointButton) {
                openAddPointWindow(latitude, longitude);
            }
        });
    }

    private void openAddPointWindow(double latitude, double longitude) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_point.fxml"));
            Parent root = loader.load();
            AddPointController controller = loader.getController();
            controller.setData(requestHandler, mapGraph.getGraphId());

            controller.setInitialCoordinates(latitude, longitude);

            Stage stage = new Stage();
            stage.setTitle("Add New Point");
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> Platform.runLater(this::updateMap));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAlgorithmSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/algorithm_selection.fxml"));
            Parent root = loader.load();
            AlgorithmSelectionController controller = loader.getController();
            controller.setData(requestHandler, mapGraph.getGraphId());

            Stage stage = new Stage();
            stage.setTitle("Algorithm Selection");
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> Platform.runLater(this::updateMap));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addPoint() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_point.fxml"));
            Parent root = loader.load();
            AddPointController controller = loader.getController();
            controller.setData(requestHandler, mapGraph.getGraphId());

            Stage stage = new Stage();
            stage.setTitle("Add Point");
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> Platform.runLater(this::updateMap));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removePoint() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/remove_point.fxml"));
            Parent root = loader.load();
            RemovePointController controller = loader.getController();
            controller.setData(requestHandler, mapGraph.getGraphId(), mapGraph.getNodeList());

            Stage stage = new Stage();
            stage.setTitle("Remove Point");
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> Platform.runLater(this::updateMap));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addEdge() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_edge.fxml"));
            Parent root = loader.load();
            AddEdgeController controller = loader.getController();
            controller.setData(requestHandler, mapGraph.getGraphId(), mapGraph.getNodeList());

            Stage stage = new Stage();
            stage.setTitle("Add Edge");
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> Platform.runLater(this::updateMap));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeEdge() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/remove_edge.fxml"));
            Parent root = loader.load();
            RemoveEdgeController controller = loader.getController();
            controller.setData(requestHandler, mapGraph.getGraphId(), mapGraph);

            Stage stage = new Stage();
            stage.setTitle("Remove Edge");
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> Platform.runLater(this::updateMap));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateMap() {
        Platform.runLater(() -> {
            mapGraph = requestHandler.getMap(mapGraph.getGraphId());
            if (mapGraph != null) {
                loadNodesAndEdges();
                loadNodes();
            } else {
                System.out.println("Error: Failed to load map from server.");
            }
        });
    }

    public void setMapGraph(MapGraph mapGraph) {
        this.mapGraph = mapGraph;
        loadNodesAndEdges();
        loadNodes();
    }

    private void loadNodesAndEdges() {
        if (mapGraph == null) {
            System.out.println("No graph loaded.");
            return;
        }

        clearMap();

        Platform.runLater(() -> {
            for (Node node : mapGraph.getNodeList()) {
                Marker marker = Marker.createProvided(Marker.Provided.RED)
                        .setPosition(new Coordinate(node.getLatitude(), node.getLongitude()))
                        .setVisible(true);

                mapView.addMarker(marker);
                markers.add(marker);

                for (Map.Entry<String, Double> edge : node.getEdgesList().entrySet()) {
                    Node targetNode = mapGraph.findNodeById(edge.getKey());
                    if (targetNode != null) {
                        Coordinate start = new Coordinate(node.getLatitude(), node.getLongitude());
                        Coordinate end = new Coordinate(targetNode.getLatitude(), targetNode.getLongitude());
                        CoordinateLine edgeLine = new CoordinateLine(start, end)
                                .setColor(Color.BLUE)
                                .setWidth(2)
                                .setVisible(true);

                        mapView.addCoordinateLine(edgeLine);
                        edgeLines.add(edgeLine);
                    }
                }
            }
            System.out.println("Loaded " + mapGraph.getNodeList().size() + " nodes and " + edgeLines.size() + " edges.");
        });
    }


    private void clearMap() {
        Platform.runLater(() -> {
            markers.forEach(mapView::removeMarker);
            edgeLines.forEach(mapView::removeCoordinateLine);

            markers.clear();
            edgeLines.clear();
        });
    }



    private void loadNodes() {
        if (mapGraph == null) {
            System.out.println("Error: No map loaded.");
            return;
        }

        List<Node> nodes = mapGraph.getNodeList();

        Platform.runLater(() -> {
            comboStart.getItems().clear();
            comboDest.getItems().clear();

            for (Node node : nodes) {
                comboStart.getItems().add(node.getId());
                comboDest.getItems().add(node.getId());
            }
        });

        System.out.println("Loaded " + nodes.size() + " nodes into ComboBox.");
    }

    @FXML
    private void calculateRoute() {
        String startNodeId = comboStart.getValue();
        String destNodeId = comboDest.getValue();

        if (startNodeId == null || destNodeId == null) {
            System.out.println("Error: Please select both start and destination nodes.");
            return;
        }

        System.out.println("Calculating shortest path from " + startNodeId + " to " + destNodeId);

        DeliveryRoute shortestPathResponse = requestHandler.calculateRoute(mapGraph.getGraphId(), startNodeId, destNodeId);
        List<Node> shortestPath=shortestPathResponse.path;
        if (shortestPath == null || shortestPath.isEmpty()) {
            System.out.println("Error: No route found.");
            routePathLabel.setText("No Route Found");
            totalDistanceLabel.setText("N/A");
            totalTimeLabel.setText("N/A");
            return;
        }

        double totalDistance = calculateTotalDistance(shortestPath);
        double totalTime = totalDistance / 80;

        Platform.runLater(() -> {
            routePathLabel.setText("Route: "+formatRoute(shortestPath));
            totalDistanceLabel.setText(String.format("Total Distance: %.2f km", totalDistance));
            totalTimeLabel.setText(String.format("Total Time: %.2f hours", totalTime));
        });

        System.out.println("Route: " + formatRoute(shortestPath));
        System.out.println("Total Distance: " + totalDistance + " km");
        System.out.println("Total Time: " + totalTime + " hours");
    }


    private double calculateTotalDistance(List<Node> path) {
        double totalDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);
            Double weight = current.getEdgesList().get(next.getId());
            if (weight != null) {
                totalDistance += weight;
            }
        }
        return totalDistance;
    }


    private String formatRoute(List<Node> path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i).getId());
            if (i < path.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
}
