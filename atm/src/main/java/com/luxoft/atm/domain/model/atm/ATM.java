package com.luxoft.atm.domain.model.atm;

import com.luxoft.atm.domain.model.Denomination;
import com.luxoft.atm.domain.model.banknote.Banknote;
import com.luxoft.atm.domain.model.banknote.BanknotesBox;

import java.util.List;
import java.util.Set;

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
    void take(Banknote banknote) throws ATMBanknotesBoxNotFoundException;

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
    List<Banknote> give(int sum) throws ATMIncorrectSumException, ATMInsufficientBalanceException;

    /**
     * Gets a balance of this ATM
     *
     * @return a balance of this ATM
     */
    int getBalance();
}
