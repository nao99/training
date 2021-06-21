package com.luxoft.orders.domain.model;

/**
 * OrderItemName class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class OrderItemName {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 200;

    /**
     * Name
     */
    private final String name;

    /**
     * OrderItemName constructor
     *
     * @param name a name
     * @throws IllegalArgumentException if order item name is invalid
     */
    private OrderItemName(String name) {
        if (name.length() < MIN_NAME_LENGTH) {
            String messagePattern = "Order username length must be greater than %d, but %d given";
            throw new IllegalArgumentException(String.format(messagePattern, MIN_NAME_LENGTH, name.length()));
        }

        if (name.length() > MAX_NAME_LENGTH) {
            String messagePattern = "Order username length must be less than %d, but %d given";
            throw new IllegalArgumentException(String.format(messagePattern, MAX_NAME_LENGTH, name.length()));
        }

        this.name = name;
    }

    /**
     * OrderItemName static constructor
     *
     * @param name a name
     * @throws IllegalArgumentException if order item name is empty
     */
    public static OrderItemName of(String name) {
        return new OrderItemName(name);
    }

    public String name() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (!other.getClass().isAssignableFrom(OrderItemName.class)) {
            return false;
        }

        OrderItemName otherOrderItemName = (OrderItemName) other;

        return name.equals(otherOrderItemName.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * 17 + name.hashCode();
    }
}
