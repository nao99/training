package com.luxoft.orders;

/**
 * OrderPositionExistsException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-17
 */
public class OrderPositionExistsException extends Exception {
    /**
     * OrderPositionExistsException constructor
     *
     * @param message an exception message
     */
    public OrderPositionExistsException(String message) {
        super(message);
    }
}
