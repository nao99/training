package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.domain.model.OrderItemCount;

/**
 * ReplacePositionCountService interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface ReplacePositionCountService {
    /**
     * Replaces an {@link OrderItem} count
     *
     * @param orderItem an order item
     * @param count     a replaceable count
     */
    void replace(OrderItem orderItem, OrderItemCount count);
}
