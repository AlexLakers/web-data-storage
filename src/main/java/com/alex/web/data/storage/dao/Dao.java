package com.alex.web.data.storage.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 *This is DAO interface.It defines the main functions for interaction with the database.
 * @param <K> key type
 * @param <E> entity type
 */

public interface Dao <K,E>{
    /**
     *  Returns a persistent entity with id if given entity is saved in the database.
     * @param entity a specific(transient) entity
     * @return persistent entity with id
     */
    E save(Connection connection,E entity);

    /**
     * Returns an updated entity if given changed entity is available in the database.
     * @param  entity changed entity
     * @return updated entity
     */
    E update(Connection connection,E entity);

    /**
     * Returns boolean result of deleting operation from the database.Delete a specific entity by id.
     * @param key id of deleted entity
     * @return boolean result of deleting.If the entity is deleted then result is true,else -false.
     */
    boolean delete(Connection connection,K key);

    /**
     * Returns list of entities.It contains all the entities that are available in the database.
     * @return list of entities.
     */
    List<E> findAll(Connection connection);

    /**
     * Returns {@link Optional optional} with entity that's found by id.
     * @param key id for search entity in the database.
     * @return optional with entity if entity is available in the database by id,else this method returns an empty optional.
     */
    Optional<E> findById(Connection connection,K key);

}
