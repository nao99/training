package com.luxoft.orders.infrastructure;

/**
 * DataAccessException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class DataAccessException extends RuntimeException {
    /**
     * DataAccessException constructor
     *
     * @param message  an exception message
     * @param previous a previous exception
     */
    public DataAccessException(String message, Throwable previous) {
        super(message, previous);
    }
}
