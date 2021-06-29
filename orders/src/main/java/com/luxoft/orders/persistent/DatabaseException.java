package com.luxoft.orders.persistent;

/**
 * DatabaseException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable previous) {
        super(message, previous);
    }
}
