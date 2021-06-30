package com.luxoft.atm.domain.model.banknote;

import com.luxoft.atm.domain.model.Denomination;
import lombok.Builder;
import lombok.Getter;

import java.util.*;

/**
 * DefaultBanknotesBox class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-29
 */
@Getter
@Builder
public class DefaultBanknotesBox implements BanknotesBox {
    private final Denomination denomination;
    private final Deque<Banknote> banknotes;
    private final UUID uuid;

    private DefaultBanknotesBox(
        Denomination denomination,
        Deque<Banknote> banknotes,
        UUID uuid
    ) throws IllegalArgumentException {
        if (denomination == null) {
            throw new IllegalArgumentException("Banknotes box's denomination must not be null");
        }

        this.denomination = denomination;
        this.banknotes = banknotes == null ? new ArrayDeque<>() : banknotes;
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    }

    @Override
    public void take(Banknote banknote) throws BanknoteBoxDenominationException {
        if (banknote.getDenomination() != denomination) {
            throw new BanknoteBoxDenominationException("Banknote and banknotes box denominations must be equal");
        }

        banknotes.push(banknote);
    }

    @Override
    public Banknote give() throws BanknoteBoxEmptyException {
        if (banknotes.isEmpty()) {
            throw new BanknoteBoxEmptyException("Banknotes box is empty");
        }

        return banknotes.pop();
    }

    @Override
    public int getWorth() {
        return banknotes.size() * denomination.toInt();
    }

    @Override
    public boolean empty() {
        return banknotes.size() == 0;
    }

    @Override
    public String toString() {
        return "DefaultBanknotesBox{" +
            "denomination=" + denomination +
            ", uuid=" + uuid +
            '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        DefaultBanknotesBox banknotesBox = (DefaultBanknotesBox) other;

        return Objects.equals(uuid, banknotesBox.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
