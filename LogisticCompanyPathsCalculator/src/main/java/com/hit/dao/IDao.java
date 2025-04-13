package main.java.com.hit.dao;

import java.util.List;

public interface IDao<T> {
    void save(T obj);
    void delete(String id);
    T getById(String id);
    List<T> getAll();
}
