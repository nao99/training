package com.luxoft.orders.domain.model;

/**
 * OrderItemCount class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class OrderItemCount {
    /**
     * Count
     */
    private final int count;

    /**
     * OrderItemCount constructor
     *
     * @param count a count
     * @throws IllegalArgumentException if the count less than or equal to 0
     */
    private OrderItemCount(int count) {
        if (count <= 0) {
            String errorMessage = String.format("Order item count must be greater than 0, but %d given", count);
            throw new IllegalArgumentException(errorMessage);
        }

        this.count = count;
    }

    /**
     * OrderItemCount static constructor
     *
     * @param count a count
     */
    public static OrderItemCount of(int count) {
        return new OrderItemCount(count);
    }

    public int count() {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (!other.getClass().isAssignableFrom(OrderItemCount.class)) {
            return false;
        }

        OrderItemCount otherOrderItemCount = (OrderItemCount) other;

        return count == otherOrderItemCount.count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * 17 + count;
    }
}
