package com.luxoft.orders.infrastructure;

/**
 * DatabaseException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
public class DatabaseException extends RuntimeException {
    /**
     * DatabaseException constructor
     *
     * @param message  an exception message
     * @param previous a previous exception
     */
    public DatabaseException(String message, Throwable previous) {
        super(message, previous);
    }
}
