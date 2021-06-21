package com.luxoft.orders.domain.model;

/**
 * OrderItemId class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class OrderItemId {
    /**
     * Id
     */
    private final long id;

    /**
     * OrderItemId constructor
     *
     * @param id an id
     * @throws IllegalArgumentException if an order item id is less than or equal to 0
     */
    private OrderItemId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format("Order id must be greater than 0, but %d given", id));
        }

        this.id = id;
    }

    /**
     * OrderItemId static constructor
     *
     * @param id an id
     */
    public static OrderItemId of(long id) {
        return new OrderItemId(id);
    }

    public long id() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (!other.getClass().isAssignableFrom(OrderItemId.class)) {
            return false;
        }

        OrderItemId otherOrderItemId = (OrderItemId) other;

        return id == otherOrderItemId.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * 17 + Long.hashCode(id);
    }
}
