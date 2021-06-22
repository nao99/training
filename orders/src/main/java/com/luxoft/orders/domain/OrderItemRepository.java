package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.DataAccessException;

import java.sql.Connection;
import java.util.Optional;
import java.util.Set;

/**
 * OrderItemRepository interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface OrderItemRepository {
    /**
     * Finds an {@link OrderItem} by id
     *
     * @param connection a connection
     * @param id         an order item id
     *
     * @return an order item if exists
     * @throws DataAccessException if something was wrong
     */
    Optional<OrderItem> findById(Connection connection, Long id) throws DataAccessException;

    /**
     * Finds {@link OrderItem}s by order id
     *
     * @param connection a connection
     * @param orderId    an order id
     *
     * @return a set of order items
     * @throws DataAccessException if something was wrong
     */
    Set<OrderItem> findByOrderId(Connection connection, Long orderId) throws DataAccessException;

    /**
     * Saves an {@link OrderItem}
     *
     * @param connection a connection
     * @param item       an order item
     *
     * @return a saved order item
     * @throws DataAccessException if something was wrong
     */
    OrderItem save(Connection connection, OrderItem item) throws DataAccessException;
}
