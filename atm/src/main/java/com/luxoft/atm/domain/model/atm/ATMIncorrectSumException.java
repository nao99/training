package com.luxoft.atm.domain.model.atm;

/**
 * ATMIncorrectSumException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public class ATMIncorrectSumException extends ATMException {
    public ATMIncorrectSumException(String message) {
        super(message);
    }
}
