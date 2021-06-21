package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderId;

/**
 * GetOrderService interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface GetOrderService {
    /**
     * Gets an {@link Order}
     *
     * @param id an order id
     *
     * @return an order if exists
     * @throws OrderNotFoundException if order was not found
     */
    Order getOrder(OrderId id) throws OrderNotFoundException;
}
