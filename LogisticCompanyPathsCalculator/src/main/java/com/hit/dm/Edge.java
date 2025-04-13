package main.java.com.hit.dm;


import main.java.com.hit.algorithm.Node;

public class Edge {
    private Node source;
    private Node destination;
    private double weight;

    public Edge(Node source, Node destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Node getSource() { return source; }
    public Node getDestination() { return destination; }
    public double getWeight() { return weight; }
}
