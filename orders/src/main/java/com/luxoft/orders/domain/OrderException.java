package com.luxoft.orders.domain;

/**
 * OrderException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class OrderException extends Exception {
    /**
     * OrderException constructor
     *
     * @param message an exception message
     */
    public OrderException(String message) {
        super(message);
    }

    /**
     * OrderException constructor
     *
     * @param message  an exception message
     * @param previous a previous exception
     */
    public OrderException(String message, Throwable previous) {
        super(message, previous);
    }
}
