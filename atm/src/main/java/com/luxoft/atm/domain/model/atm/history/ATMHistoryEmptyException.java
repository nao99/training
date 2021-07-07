package com.luxoft.atm.domain.model.atm.history;

import com.luxoft.atm.domain.model.atm.ATMException;

/**
 * ATMHistoryEmptyException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-07
 */
public class ATMHistoryEmptyException extends ATMException {
    public ATMHistoryEmptyException(String message, Throwable previous) {
        super(message, previous);
    }
}
