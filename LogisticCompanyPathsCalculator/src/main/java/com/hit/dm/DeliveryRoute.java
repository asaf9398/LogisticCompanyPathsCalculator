package main.java.com.hit.dm;

import main.java.com.hit.algorithm.Node;

import java.util.List;

/**
 */
public class DeliveryRoute {
    private List<Node> path;
    private List<Node> deliveryPoints;

    public DeliveryRoute(List<Node> path, List<Node> deliveryPoints) {
        this.path = path;
        this.deliveryPoints = deliveryPoints;
    }

    public List<Node> getPath() { return path; }
    public List<Node> getDeliveryPoints() { return deliveryPoints; }
}
