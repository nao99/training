package com.luxoft.atm.domain.model.atm;

/**
 * ATMBanknotesBoxNotFoundException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public class ATMBanknotesBoxNotFoundException extends ATMException {
    public ATMBanknotesBoxNotFoundException(String message) {
        super(message);
    }
}
