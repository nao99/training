package com.luxoft.atm.domain.model.atm;

/**
 * ATMInsufficientBalanceException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public class ATMInsufficientBalanceException extends ATMException {
    public ATMInsufficientBalanceException(String message) {
        super(message);
    }
}
