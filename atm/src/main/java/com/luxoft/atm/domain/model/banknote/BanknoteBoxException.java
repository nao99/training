package com.luxoft.atm.domain.model.banknote;

/**
 * BanknoteBoxException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public class BanknoteBoxException extends RuntimeException {
    public BanknoteBoxException(String message) {
        super(message);
    }
}
