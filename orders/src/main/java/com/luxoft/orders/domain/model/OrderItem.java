package com.luxoft.orders.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * OrderItem class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
@Getter
@Builder
public class OrderItem {
    private final Long id;
    private final Long orderId;
    private final String name;
    private int count;
    private final BigDecimal price;

    private OrderItem(Long id, Long orderId, String name, int count, BigDecimal price) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public OrderItem withId(Long id) {
        return new OrderItem(id, orderId, name, count, price);
    }

    public OrderItem withOrderId(Long orderId) {
        return new OrderItem(id, orderId, name, count, price);
    }

    /**
     * Changes a count of this item
     *
     * @param newCount a changeable count
     */
    public void changeCount(int newCount) {
        count = newCount;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + id +
            ", orderId=" + orderId +
            ", name='" + name + '\'' +
            ", count=" + count +
            ", price=" + price +
            '}';
    }
}
