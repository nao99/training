package com.luxoft.orders.api;

import com.luxoft.orders.domain.model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderDto class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-31
 */
public class OrderDto {
    private final Order order;

    public OrderDto(Order order) {
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

    public LocalDateTime getUpdatedAt() {
        return order.getUpdatedAt();
    }

    public List<OrderItemDto> getItems() {
        return order.getItems().stream()
            .map(OrderItemDto::new)
            .collect(Collectors.toList());
    }
}
