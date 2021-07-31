package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.OrderItem;

import java.util.Optional;

/**
 * OrderItemRepositoryCustom interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-02
 */
public interface OrderItemRepositoryCustom {
    /**
     * Finds an {@link OrderItem} by id and locks it
     *
     * @param id an order item id
     * @return an order item if exists
     */
    Optional<OrderItem> findByIdAndLock(Long id);
}
