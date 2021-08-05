package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * OrderItemRepositoryCustomImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-02
 */
@Repository
public class OrderItemRepositoryCustomImpl implements OrderItemRepositoryCustom {
    private final EntityManager entityManager;

    @Autowired
    public OrderItemRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<OrderItem> findByIdAndLock(Long id) {
        return Optional.ofNullable(entityManager.find(OrderItem.class, id, LockModeType.PESSIMISTIC_READ));
    }
}
