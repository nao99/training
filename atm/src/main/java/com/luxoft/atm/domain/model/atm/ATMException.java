package com.luxoft.atm.domain.model.atm;

/**
 * ATMException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public class ATMException extends RuntimeException {
    public ATMException(String message) {
        super(message);
    }
}
