package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.persistent.DataAccessException;
import org.hibernate.LockMode;
import org.hibernate.Session;

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
     * @param session  a session
     * @param id       an order id
     * @param lockMode a lock mode
     *
     * @return an order if exists
     */
    Optional<Order> findById(Session session, Long id, LockMode lockMode);

    /**
     * Saves an {@link Order}
     *
     * @param session a session
     * @param order   an order
     */
    void save(Session session, Order order);

    /**
     * Updates an {@link Order} timestamp
     *
     * @param session a session
     * @param orderId an order id
     *
     * @throws DataAccessException if something was wrong
     */
    void updateOrderTimestamp(Session session, Long orderId) throws DataAccessException;

    /**
     * Finds a count of {@link Order}s
     *
     * @param session a session
     *
     * @return a count of orders
     * @throws DataAccessException if something was wrong
     */
    long countNonDone(Session session) throws DataAccessException;

    /**
     * Marks as done all non done {@link Order}s
     *
     * @param session   a session
     * @param batchSize a size of batch
     *
     * @throws DataAccessException if something was wrong
     */
    void doneAllNonDoneOrdersBatched(Session session, int batchSize) throws DataAccessException;
}
