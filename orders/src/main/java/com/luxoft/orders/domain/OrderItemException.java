package com.luxoft.orders.domain;

/**
 * OrderItemException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-24
 */
public class OrderItemException extends OrderException {
    public OrderItemException(String message) {
        super(message);
    }
}
