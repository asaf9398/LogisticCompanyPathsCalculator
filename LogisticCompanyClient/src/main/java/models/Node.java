package models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Node {
    public String id;
    public String name;
    public double latitude;
    public double longitude;
    public Map<String, Double> edgesList;

    public Node(String id) {
        this(id, 0, 0);
    }

    public Node(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edgesList = new HashMap<>();
    }

    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.name = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edgesList = new HashMap<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public Map<String, Double> getEdgesList() { // שינוי: המפתח הוא `String`
        return edgesList;
    }

    public void addEdge(String neighborId, double weight) { // שינוי: שימוש במזהה
        edgesList.put(neighborId, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node that = (Node) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
