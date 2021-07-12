package com.luxoft.atm.domain.model.atm;

import com.luxoft.atm.domain.model.Denomination;
import com.luxoft.atm.domain.model.atm.history.ATMHistoryEmptyException;
import com.luxoft.atm.domain.model.banknote.Banknote;
import com.luxoft.atm.domain.model.banknote.BanknotesBox;

import java.util.List;

/**
 * ATM interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public interface ATM {
    /**
     * Takes a {@link Banknote}
     *
     * @param banknote a banknote
     * @throws ATMBanknotesBoxNotFoundException if unable to find any {@link BanknotesBox}
     *                                          for banknote's {@link Denomination}
     */
    void take(Banknote banknote) throws ATMBanknotesBoxNotFoundException, ATMDisabledException;

    /**
     * Gives {@link Banknote}s for specified sum
     *
     * @param sum a sum to give
     *
     * @return a list of banknotes
     *
     * @throws ATMIncorrectSumException if a sum is incorrect (e.g. 453, -1, 0, etc.)
     * @throws ATMInsufficientBalanceException if balance is not enough for withdrawing (e.g. 0)
     */
    List<Banknote> give(int sum) throws ATMIncorrectSumException, ATMInsufficientBalanceException, ATMDisabledException;

    /**
     * Gets a balance of this ATM
     *
     * @return a balance of this ATM
     */
    int getBalance() throws ATMDisabledException;

    /**
     * Does a backup to keep current state
     */
    void backup() throws ATMDisabledException;

    /**
     * Restores a last done backup
     */
    void restore() throws ATMHistoryEmptyException, ATMDisabledException;

    /**
     * Disables this ATM
     */
    void disable();

    /**
     * Enables this ATM
     */
    void enable();
}
