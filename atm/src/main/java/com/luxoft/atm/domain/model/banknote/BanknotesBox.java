package com.luxoft.atm.domain.model.banknote;

import com.luxoft.atm.domain.model.Denomination;

import java.util.Comparator;

/**
 * BanknotesBox interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public interface BanknotesBox {
    /**
     * Takes a {@link Banknote}
     *
     * @param banknote a banknote to take
     * @throws BanknoteBoxDenominationException if a banknote has not the same denomination as a box
     */
    void take(Banknote banknote) throws BanknoteBoxDenominationException;

    /**
     * Gives a {@link Banknote} from this box
     *
     * @return a banknote
     * @throws BanknoteBoxEmptyException if the box is empty
     */
    Banknote give() throws BanknoteBoxEmptyException;

    /**
     * Gets a worth of this box
     *
     * @return a box worth
     */
    int getWorth();

    /**
     * Gets a {@link Denomination} of this box
     *
     * @return a box denomination
     */
    Denomination getDenomination();

    /**
     * Checks if this box is empty
     *
     * @return true if box is empty or false else
     */
    boolean empty();

    /**
     * Gets a {@link Comparator} to compare 2 boxes <br>
     * Compares by denomination (DESC)
     *
     * @return a boxes comparator
     */
    static Comparator<? super BanknotesBox> comparator() {
        return (b1, b2) -> b2.getDenomination().lessThan(b1.getDenomination()) ? -1 : 0;
    }
}
