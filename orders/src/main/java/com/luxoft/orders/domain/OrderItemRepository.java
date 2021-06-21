package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.OrderId;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.domain.model.OrderItemId;
import com.luxoft.orders.domain.model.OrderItemName;
import com.luxoft.orders.infrastructure.DataAccessException;

import java.sql.Connection;
import java.util.Optional;

/**
 * OrderItemRepository interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface OrderItemRepository {
    /**
     * Finds an {@link OrderItem} by an {@link OrderItemId}
     *
     * @param connection a connection
     * @param id         an order item id
     *
     * @return an order item if exists
     * @throws DataAccessException if something was wrong
     */
    Optional<OrderItem> find(Connection connection, OrderItemId id) throws DataAccessException;

    /**
     * Finds an {@link OrderItem} by an {@link OrderItemId} and {@link OrderItemName}
     *
     * @param connection a connection
     * @param orderId    an order id
     * @param name       a name
     *
     * @return an order item
     * @throws DataAccessException if something was wrong
     */
    Optional<OrderItem> findByOrderIdAndName(
        Connection connection,
        OrderId orderId,
        OrderItemName name
    ) throws DataAccessException;

    /**
     * Finds {@link OrderItem}s by an {@link OrderId}
     *
     * @param connection a connection
     * @param orderId    an order id
     *
     * @return an iterable of order items
     * @throws DataAccessException if something was wrong
     */
    Iterable<OrderItem> findByOrderId(Connection connection, OrderId orderId) throws DataAccessException;

    /**
     * Inserts a new {@link OrderItem}
     *
     * @param connection a connection
     * @param orderItem  an order item
     *
     * @return the same order item with generated id
     * @throws DataAccessException if something was wrong
     */
    OrderItem insert(Connection connection, OrderItem orderItem) throws DataAccessException;

    /**
     * Updates an {@link OrderItem}
     *
     * @param connection a connection
     * @param orderItem  an order item
     *
     * @throws DataAccessException if something was wrong
     */
    void update(Connection connection, OrderItem orderItem) throws DataAccessException;
}
