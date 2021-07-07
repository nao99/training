package com.luxoft.atm.domain.model.atm;

import com.luxoft.atm.domain.model.atm.history.ATMHistory;
import com.luxoft.atm.domain.model.atm.history.ATMHistoryEmptyException;
import com.luxoft.atm.domain.model.atm.history.ATMSnapshot;
import com.luxoft.atm.domain.model.banknote.Banknote;
import com.luxoft.atm.domain.model.banknote.BanknotesBox;
import com.luxoft.atm.domain.model.banknote.Box;
import lombok.Builder;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DefaultATM class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
@Builder
public class DefaultATM implements ATM {
    private Box box;
    private final UUID uuid;
    private boolean enabled;

    private final ATMHistory history;

    private DefaultATM(Box box, UUID uuid, boolean enabled, ATMHistory history) {
        this.box = box == null ? Box.builder().build() : box;
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.enabled = enabled;
        this.history = history == null ? ATMHistory.builder().build() : history;
    }

    public Box getBox() {
        return box;
    }

    @Override
    public void take(Banknote banknote) throws ATMBanknotesBoxNotFoundException, ATMDisabledException {
        throwIfDisabled();

        for (var banknoteBox : box.getBanknoteBoxes()) {
            if (banknoteBox.getDenomination() == banknote.getDenomination()) {
                banknoteBox.take(banknote);
                return;
            }
        }

        var errorMessagePattern = "A suitable banknote box not found for the \"%s\" banknote denomination";
        throw new ATMBanknotesBoxNotFoundException(String.format(errorMessagePattern, banknote.getDenomination()));
    }

    @Override
    public List<Banknote> give(
        int sum
    ) throws ATMIncorrectSumException, ATMInsufficientBalanceException, ATMDisabledException {
        throwIfDisabled();

        if (sum <= 0) {
            throw new ATMIncorrectSumException(String.format("Sum must be greater than 0, but %d given", sum));
        }

        var balance = getBalance();
        if (balance < sum) {
            throw new ATMInsufficientBalanceException("The ATM balance is less than required sum");
        }

        var banknoteBoxesSortedList = box.getBanknoteBoxes().stream()
            .sorted(BanknotesBox.comparator())
            .collect(Collectors.toList());

        var possibleToGive = checkIfPossibleToGive(sum, banknoteBoxesSortedList);
        if (!possibleToGive) {
            throw new ATMIncorrectSumException(String.format("The %d sum cannot be given by no one denomination", sum));
        }

        List<Banknote> banknotes = new ArrayList<>();

        // TODO: Avoid the code's duplication
        // TODO: Move this algorithm to a separated place
        for (var banknoteBox : banknoteBoxesSortedList) {
            var banknotesBoxDenomination = banknoteBox.getDenomination();
            var banknotesBoxDenominationInt = banknotesBoxDenomination.toInt();

            while (!banknoteBox.empty() && sum >= banknotesBoxDenominationInt) {
                banknotes.add(banknoteBox.give());
                sum -= banknotesBoxDenominationInt;
            }
        }

        return banknotes;
    }

    @Override
    public int getBalance() throws ATMDisabledException {
        throwIfDisabled();
        return box.getBanknoteBoxes().stream()
            .map(BanknotesBox::getWorth)
            .reduce(0, Integer::sum);
    }

    @Override
    public void backup() throws ATMDisabledException {
        throwIfDisabled();

        var boxClone = SerializationUtils.clone(box);
        var snapshot = ATMSnapshot.builder()
            .box(boxClone)
            .build();

        history.push(snapshot);
    }

    @Override
    public void restore() throws ATMHistoryEmptyException, ATMDisabledException {
        throwIfDisabled();

        var snapshot = history.pop();
        replaceBox(snapshot.getBox());
    }

    public boolean enabled() {
        return enabled;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    private void throwIfDisabled() throws ATMDisabledException {
        if (!enabled) {
            throw new ATMDisabledException("ATM is disabled now");
        }
    }

    private void replaceBox(Box box) {
        this.box = box;
    }

    private boolean checkIfPossibleToGive(int sum, List<BanknotesBox> banknoteBoxes) {
        for (var banknoteBox : banknoteBoxes) {
            var banknotesBoxDenomination = banknoteBox.getDenomination();
            var banknotesBoxDenominationInt = banknotesBoxDenomination.toInt();

            while (!banknoteBox.empty() && sum >= banknotesBoxDenominationInt) {
                sum -= banknotesBoxDenominationInt;
            }
        }

        return sum == 0;
    }

    @Override
    public String toString() {
        return "DefaultATM{" +
            "uuid=" + uuid +
            ", enabled=" + enabled +
            '}';
    }
}
