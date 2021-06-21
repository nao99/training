package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderUsername;

/**
 * CreateOrderService interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface CreateOrderService {
    /**
     * Creates a new {@link Order}
     *
     * @param username a username
     * @return a new order
     */
    Order create(OrderUsername username);
}
