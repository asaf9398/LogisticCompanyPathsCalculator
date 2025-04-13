package main.java.com.hit.dm;

import main.java.com.hit.algorithm.Node;

import java.util.List;

public class DeliveryRequest {
    private Node start;
    private Node destination;
    private List<Node> additionalStops;

    public DeliveryRequest(Node start, Node destination, List<Node> additionalStops) {
        this.start = start;
        this.destination = destination;
        this.additionalStops = additionalStops;
    }

    public Node getStart() { return start; }
    public Node getDestination() { return destination; }
    public List<Node> getAdditionalStops() { return additionalStops; }
}
