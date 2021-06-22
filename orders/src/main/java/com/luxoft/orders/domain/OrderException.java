package com.luxoft.orders.domain;

/**
 * OrderException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class OrderException extends Exception {
    public OrderException(String message) {
        super(message);
    }
}
