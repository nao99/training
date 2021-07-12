package com.luxoft.atm.domain.model.banknote;

/**
 * BanknoteBoxEmptyException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public class BanknoteBoxEmptyException extends BanknoteBoxException {
    public BanknoteBoxEmptyException(String message) {
        super(message);
    }
}
