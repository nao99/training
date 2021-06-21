package com.luxoft.orders.domain.model;

/**
 * OrderId class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
public class OrderId {
    /**
     * Id
     */
    private final long id;

    /**
     * OrderId constructor
     *
     * @param id an id
     * @throws IllegalArgumentException if an order id is less than or equal to 0
     */
    private OrderId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format("Order id must be greater than 0, but %d given", id));
        }

        this.id = id;
    }

    /**
     * OrderId static constructor
     *
     * @param id an id
     */
    public static OrderId of(long id) {
        return new OrderId(id);
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

        if (!other.getClass().isAssignableFrom(OrderId.class)) {
            return false;
        }

        OrderId otherOrderId = (OrderId) other;

        return id == otherOrderId.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * 17 + Long.hashCode(id);
    }
}
