package com.luxoft.orders.domain;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.OrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;

/**
 * OrderServiceImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        return null;
    }

    @Override
    public Order create(CreateOrderDto createOrderDto) {
        return null;
    }

    @Override
    public void addOrderItem(Order order, OrderItemDto orderItemDto) {

    }

    @Override
    public void changeOrderItemCount(OrderItem orderItem, int count) {

    }

    @Override
    public void doneAllOrders() {

    }
}
