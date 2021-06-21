package com.luxoft.orders.domain.model;

/**
 * OrderUsername class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class OrderUsername {
    private static final int MIN_USERNAME_LENGTH = 2;
    private static final int MAX_USERNAME_LENGTH = 200;

    /**
     * Username
     */
    private final String username;

    /**
     * OrderUsername constructor
     *
     * @param username a username
     * @throws IllegalArgumentException if username is invalid
     */
    private OrderUsername(String username) {
        if (username.length() < MIN_USERNAME_LENGTH) {
            String messagePattern = "Order username length must be greater than %d, but %d given";
            throw new IllegalArgumentException(String.format(messagePattern, MIN_USERNAME_LENGTH, username.length()));
        }

        if (username.length() > MAX_USERNAME_LENGTH) {
            String messagePattern = "Order username length must be less than %d, but %d given";
            throw new IllegalArgumentException(String.format(messagePattern, MAX_USERNAME_LENGTH, username.length()));
        }

        this.username = username;
    }

    /**
     * OrderUsername static constructor
     *
     * @param username a username
     * @throws IllegalArgumentException if order item username is empty
     */
    public static OrderUsername of(String username) {
        return new OrderUsername(username);
    }

    public String username() {
        return username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (!other.getClass().isAssignableFrom(OrderUsername.class)) {
            return false;
        }

        OrderUsername otherOrderUsername = (OrderUsername) other;

        return username.equals(otherOrderUsername.username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * 17 + username.hashCode();
    }
}
