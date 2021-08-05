package com.luxoft.orders.api;

import com.luxoft.orders.domain.model.OrderItem;

import java.math.BigDecimal;

/**
 * OrderItemDto class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-31
 */
public class OrderItemDto {
    private final OrderItem item;

    public OrderItemDto(OrderItem item) {
        this.item = item;
    }

    public static OrderItemDto of(OrderItem item) {
        return new OrderItemDto(item);
    }

    public Long getId() {
        return item.getId();
    }

    public String getName() {
        return item.getName();
    }

    public int getCount() {
        return item.getCount();
    }

    public BigDecimal getPrice() {
        return item.getPrice();
    }
}
