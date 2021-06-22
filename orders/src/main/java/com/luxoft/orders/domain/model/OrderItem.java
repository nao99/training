package com.luxoft.orders.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * OrderItem class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
public class OrderItem {
    private Long id;

    private Long orderId;

    private String itemName;

    private int count;

    private BigDecimal price;

    private OrderItem(Long id, Long orderId, String itemName, int count, BigDecimal price) {
        this.id = id;
        this.orderId = orderId;
        this.itemName = itemName;
        this.count = count;
        this.price = price;
    }

    public static OrderItem of(Long id, Long orderId, String itemName, int count, BigDecimal price) {
        return new OrderItem(id, orderId, itemName, count, price);
    }

    public static OrderItem of(String itemName, int count, BigDecimal price) {
        return new OrderItem(null, null, itemName, count, price);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return itemName;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (!other.getClass().isAssignableFrom(OrderItem.class)) {
            return false;
        }

        OrderItem otherOrderItem = (OrderItem) other;

        return Objects.equals(this.id, otherOrderItem.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
