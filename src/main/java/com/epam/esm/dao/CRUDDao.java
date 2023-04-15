package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface CRUDDao<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    Optional<T> save(T entity);

    T update(T entity);

    void delete(T entity);
}
