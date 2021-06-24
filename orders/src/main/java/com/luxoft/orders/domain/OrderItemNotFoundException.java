package com.luxoft.orders.domain;

/**
 * OrderItemNotFoundException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-24
 */
public class OrderItemNotFoundException extends OrderItemException {
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
