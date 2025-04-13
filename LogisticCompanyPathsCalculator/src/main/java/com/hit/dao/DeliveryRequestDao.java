package main.java.com.hit.dao;

import main.java.com.hit.dm.DeliveryRequest;

import java.util.*;

public class DeliveryRequestDao implements IDao<DeliveryRequest> {
    private Map<String, DeliveryRequest> requests = new HashMap<>();

    @Override
    public void save(DeliveryRequest request) {
        requests.put(request.getStart().getId() + "-" + request.getDestination().getId(), request);
    }

    @Override
    public void delete(String id) {
        requests.remove(id);
    }

    @Override
    public DeliveryRequest getById(String id) {
        return requests.get(id);
    }

    @Override
    public List<DeliveryRequest> getAll() {
        return new ArrayList<>(requests.values());
    }
}
