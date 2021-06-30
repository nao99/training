package com.luxoft.atm.domain;

import com.luxoft.atm.domain.model.atm.*;
import com.luxoft.atm.domain.model.banknote.Banknote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ATMServiceImpl interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-01
 */
public class ATMServiceImpl implements ATMService {
    private static final Logger logger = LoggerFactory.getLogger(ATMServiceImpl.class);

    @Override
    public Set<Banknote> deposit(Set<Banknote> banknotes, ATM atm) {
        logger.info("Deposit {} banknotes to the {} atm", banknotes, atm);

        Set<Banknote> nonDepositedBanknotes = new HashSet<>();
        for (var banknote : banknotes) {
            try {
                atm.take(banknote);
            } catch (ATMBanknotesBoxNotFoundException e) {
                logger.error("Unable to deposit {} banknote to the {} atm: {}", banknote, atm, e.getMessage());
                nonDepositedBanknotes.add(banknote);
            }
        }

        return nonDepositedBanknotes;
    }

    @Override
    public List<Banknote> withdraw(int sum, ATM atm) throws ATMException {
        logger.info("Withdraw {} sum from the {} atm", sum, atm);
        try {
            var banknotes = atm.give(sum);
            logger.info("Banknotes {} were successfully withdrawn from the {} atm", banknotes, atm);

            return banknotes;
        } catch (ATMInsufficientBalanceException e) {
            logger.warn("Balance of the {} atm is too low", atm);
            throw e;
        }
    }

    @Override
    public int checkBalance(ATM atm) {
        return atm.getBalance();
    }
}
