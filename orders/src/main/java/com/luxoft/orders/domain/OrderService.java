package com.luxoft.orders.domain;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;

/**
 * OrderService interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
public interface OrderService {
    /**
     * Gets an {@link Order}
     *
     * @param id an order id
     *
     * @return an order if exists
     * @throws OrderNotFoundException if order was not found
     */
    Order getOrder(Long id) throws OrderNotFoundException;

    /**
     * Creates a new {@link Order}
     *
     * @param createOrderDto a create order dto
     * @return a new order
     */
    Order createOrder(CreateOrderDto createOrderDto);

    /**
     * Adds an {@link OrderItem} to an {@link Order}
     *
     * @param orderId            an order id
     * @param createOrderItemDto a create order item dto
     */
    void addOrderItem(Long orderId, CreateOrderItemDto createOrderItemDto) throws OrderNotFoundException;

    /**
     * Changes an {@link OrderItem} count
     *
     * @param orderItemId an order item id
     * @param count       a changeable count
     *
     * @throws OrderItemNotFoundException if order item was not found by passed id
     */
    void changeOrderItemCount(Long orderItemId, int count) throws OrderItemNotFoundException;

    /**
     * Marks all non done {@link Order}s as done
     */
    void doneAllOrders();
}
