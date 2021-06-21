package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;

/**
 * AddPositionToOrderService interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface AddPositionToOrderService {
    /**
     * Adds an {@link OrderItem} to an {@link Order}
     *
     * @param order     an order
     * @param orderItem an order item
     *
     * @throws OrderPositionExistsException if the order already has the same position
     */
    void addPosition(Order order, OrderItem orderItem) throws OrderPositionExistsException;
}
