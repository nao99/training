package com.luxoft.orders.api;

import com.luxoft.orders.domain.model.Order;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * OrderDto class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
public class OrderDto {
    private final Order order;

    private OrderDto(Order order) {
        this.order = order;
    }

    public static OrderDto of(Order order) {
        return new OrderDto(order);
    }

    public Long getId() {
        return order.getId();
    }

    public String getUsername() {
        return order.getUsername();
    }

    public boolean isDone() {
        return order.isDone();
    }

    public Set<OrderItemDto> getItems() {
        return order.getItems().stream()
            .map(OrderItemDto::of)
            .collect(Collectors.toSet());
    }
}
