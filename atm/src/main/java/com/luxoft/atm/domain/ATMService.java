package com.luxoft.atm.domain;

import com.luxoft.atm.domain.model.atm.ATM;
import com.luxoft.atm.domain.model.atm.ATMException;
import com.luxoft.atm.domain.model.banknote.Banknote;

import java.util.List;
import java.util.Set;

/**
 * ATMService interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-01
 */
public interface ATMService {
    /**
     * Deposits a set of {@link Banknote}s to an {@link ATM}
     *
     * @param banknotes a set of banknotes
     * @param atm       an ATM into where the banknotes should be deposited
     *
     * @return a set of non deposited banknotes
     */
    Set<Banknote> deposit(Set<Banknote> banknotes, ATM atm);

    /**
     * Withdraws a sum from an {@link ATM}
     *
     * @param sum a sum to withdraw
     * @param atm an ATM from where the sum should be withdrawn
     *
     * @return a list of banknotes
     * @throws ATMException if unable to withdraw this sum (e.g. atm balance less than required sum)
     */
    List<Banknote> withdraw(int sum, ATM atm) throws ATMException;

    /**
     * Checks a balance of an {@link ATM}
     *
     * @param atm an ATM
     * @return an ATM balance
     */
    int checkBalance(ATM atm);
}
