package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.persistent.DataAccessException;
import com.luxoft.orders.persistent.LockMode;

import java.sql.Connection;
import java.util.Optional;

/**
 * OrderRepository interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface OrderRepository {
    /**
     * Finds an {@link Order} by an id
     *
     * @param connection a connection
     * @param id         an order id
     * @param mode       a locking mode (optional)
     *
     * @return an order if exists
     * @throws DataAccessException if something was wrong
     */
    Optional<Order> findById(Connection connection, Long id, LockMode mode) throws DataAccessException;

    /**
     * Saves an {@link Order}
     *
     * @param connection a connection
     * @param order      an order
     *
     * @return a saved order
     * @throws DataAccessException if something was wrong
     */
    Order save(Connection connection, Order order) throws DataAccessException;

    /**
     * Finds a count of {@link Order}s
     *
     * @param connection a connection
     *
     * @return a count of orders
     * @throws DataAccessException if something was wrong
     */
    long countNonDone(Connection connection) throws DataAccessException;

    /**
     * Dones all non done {@link Order}s
     *
     * @param connection a connection
     * @param batchSize  a size of batch
     *
     * @throws DataAccessException if something was wrong
     */
    void doneAllNonDoneOrdersBatched(Connection connection, int batchSize) throws DataAccessException;
}
