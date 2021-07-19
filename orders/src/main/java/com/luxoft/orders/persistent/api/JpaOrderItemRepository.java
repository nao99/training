package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.OrderItem;
import org.hibernate.LockMode;
import org.hibernate.Session;

import java.util.Optional;

/**
 * JpaOrderItemRepository class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-19
 */
public class JpaOrderItemRepository implements OrderItemRepository {
    @Override
    public Optional<OrderItem> findById(Session session, Long id, LockMode lockMode) {
        var orderItem = session.get(OrderItem.class, id, lockMode);
        return Optional.ofNullable(orderItem);
    }

    @Override
    public void save(Session session, OrderItem item) {
        session.save(item);
    }
}
