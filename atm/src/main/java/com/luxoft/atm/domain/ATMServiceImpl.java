package com.luxoft.atm.domain;

import com.luxoft.atm.domain.model.atm.*;
import com.luxoft.atm.domain.model.atm.history.ATMHistoryEmptyException;
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
    public Set<Banknote> deposit(Set<Banknote> banknotes, ATM atm) throws ATMDisabledException {
        logger.info("Deposit {} banknotes to the {} atm", banknotes, atm);

        Set<Banknote> nonDepositedBanknotes = new HashSet<>();
        for (var banknote : banknotes) {
            try {
                atm.take(banknote);
            } catch (ATMBanknotesBoxNotFoundException e) {
                logger.error("Unable to deposit {} banknote to the {} atm: {}", banknote, atm, e.getMessage());
                nonDepositedBanknotes.add(banknote);
            } catch (ATMDisabledException e) {
                logger.warn("ATM {} is disabled now", atm);
                throw e;
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
        } catch (ATMDisabledException e) {
            logger.warn("ATM {} is disabled now", atm);
            throw e;
        }
    }

    @Override
    public int checkBalance(ATM atm) throws ATMDisabledException {
        try {
            return atm.getBalance();
        } catch (ATMDisabledException e) {
            logger.warn("ATM {} is disabled now", atm);
            throw e;
        }
    }

    @Override
    public void backup(ATM atm) throws ATMDisabledException {
        logger.info("Do backup of the {} atm", atm);

        try {
            atm.backup();
        } catch (ATMDisabledException e) {
            logger.warn("ATM {} is disabled now", atm);
            throw e;
        }

        logger.info("Backup for the {} atm was successfully done", atm);
    }

    @Override
    public void restore(ATM atm) throws ATMHistoryEmptyException, ATMDisabledException {
        logger.info("Restore a previous state of the {} atm", atm);

        try {
            atm.restore();
        } catch (ATMDisabledException e) {
            logger.warn("ATM {} is disabled now", atm);
            throw e;
        } catch (ATMHistoryEmptyException e) {
            logger.warn(e.getMessage());
            throw e;
        }

        logger.info("ATM {} was successfully restored to the previous state", atm);
    }

    @Override
    public void disable(ATM atm) {
        logger.info("Disable the {} atm", atm);
        atm.disable();

        logger.info("The {} atm was successfully disabled", atm);
    }

    @Override
    public void enable(ATM atm) {
        logger.info("Enable the {} atm", atm);
        atm.enable();

        logger.info("The {} atm was successfully enabled", atm);
    }
}
