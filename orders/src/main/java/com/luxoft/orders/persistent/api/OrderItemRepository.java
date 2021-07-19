package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.OrderItem;
import org.hibernate.LockMode;
import org.hibernate.Session;

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
     * Finds an {@link OrderItem} by id
     *
     * @param session  a session
     * @param id       an order item id
     * @param lockMode a lock mode
     *
     * @return an order item if exists
     */
    Optional<OrderItem> findById(Session session, Long id, LockMode lockMode);

    /**
     * Saves an {@link OrderItem}
     *
     * @param session a session
     * @param item    an order item
     */
    void save(Session session, OrderItem item);
}
