package main.java.com.hit.dao;

import main.java.com.hit.algorithm.Node;

import java.util.*;

public class NodeDao implements IDao<Node> {
    private Map<String, Node> nodes = new HashMap<>();

    @Override
    public void save(Node node) {
        nodes.put(node.getId(), node);
    }

    @Override
    public void delete(String id) {
        nodes.remove(id);
    }

    @Override
    public Node getById(String id) {
        return nodes.get(id);
    }

    @Override
    public List<Node> getAll() {
        return new ArrayList<>(nodes.values());
    }
}

