package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderId;
import com.luxoft.orders.infrastructure.DataAccessException;

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
     * Finds an {@link Order} by an {@link OrderId}
     *
     * @param connection a connection
     * @param id         an order id
     *
     * @return an order if exists
     * @throws DataAccessException if something was wrong
     */
    Optional<Order> find(Connection connection, OrderId id) throws DataAccessException;

    /**
     * Inserts a new {@link Order}
     *
     * @param connection a connection
     * @param order      an order
     *
     * @return the same order with generated id
     * @throws DataAccessException if something was wrong
     */
    Order insert(Connection connection, Order order) throws DataAccessException;

    /**
     * Updates an {@link Order}
     *
     * @param connection a connection
     * @param order      an order
     *
     * @throws DataAccessException if something was wrong
     */
    void update(Connection connection, Order order) throws DataAccessException;
}
