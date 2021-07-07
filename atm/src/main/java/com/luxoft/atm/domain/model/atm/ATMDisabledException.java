package com.luxoft.atm.domain.model.atm;

/**
 * ATMDisabledException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-07
 */
public class ATMDisabledException extends ATMException {
    public ATMDisabledException(String message) {
        super(message);
    }
}
