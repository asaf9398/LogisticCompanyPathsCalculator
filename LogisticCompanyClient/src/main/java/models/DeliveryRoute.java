package models;

import java.util.List;

public class DeliveryRoute {
    public List<Node> path;

    public DeliveryRoute(List<Node> path) {
        this.path = path;
    }

    public List<Node> getPath() {
        return path;
    }
}
