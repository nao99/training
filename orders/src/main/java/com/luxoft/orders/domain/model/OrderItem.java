package com.luxoft.orders.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * OrderItem class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
public class OrderItem {
    private final Long id;
    private final Long orderId;
    private final String itemName;
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

    public static OrderItem of(Long orderId, String itemName, int count, BigDecimal price) {
        return new OrderItem(null, orderId, itemName, count, price);
    }

    public static OrderItem of(String itemName, int count, BigDecimal price) {
        return new OrderItem(null, null, itemName, count, price);
    }

    public OrderItem withId(Long id) {
        return new OrderItem(id, orderId, itemName, count, price);
    }

    public OrderItem withOrderId(Long orderId) {
        return new OrderItem(id, orderId, itemName, count, price);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
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
     * Changes a count of this item
     *
     * @param newCount a changeable count
     */
    public void changeCount(int newCount) {
        if (newCount == count) {
            return;
        }

        var pricePerOneItem = price.divide(BigDecimal.valueOf(count), RoundingMode.DOWN);
        var newPrice = pricePerOneItem.multiply(BigDecimal.valueOf(newCount));

        count = newCount;
        price = newPrice;
    }
}
