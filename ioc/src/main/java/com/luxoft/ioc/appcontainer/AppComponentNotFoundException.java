package com.luxoft.ioc.appcontainer;

/**
 * AppComponentNotFoundException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-28
 */
public class AppComponentNotFoundException extends RuntimeException {
    public AppComponentNotFoundException(String message) {
        super(message);
    }
}
