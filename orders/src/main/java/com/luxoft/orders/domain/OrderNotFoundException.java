package com.luxoft.orders.domain;

/**
 * OrderNotFoundException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-17
 */
public class OrderNotFoundException extends OrderException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
