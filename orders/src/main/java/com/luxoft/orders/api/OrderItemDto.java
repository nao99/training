package com.luxoft.orders.api;

import com.luxoft.orders.domain.model.OrderItem;

import java.math.BigDecimal;

/**
 * OrderItemDto class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
public class OrderItemDto {
    private final OrderItem item;

    private OrderItemDto(OrderItem item) {
        this.item = item;
    }

    public static OrderItemDto of(OrderItem item) {
        return new OrderItemDto(item);
    }

    public Long getId() {
        return item.getId();
    }

    public String getItemName() {
        return item.getName();
    }

    public int getItemCount() {
        return item.getCount();
    }

    public BigDecimal getItemPrice() {
        return item.getPrice();
    }
}
