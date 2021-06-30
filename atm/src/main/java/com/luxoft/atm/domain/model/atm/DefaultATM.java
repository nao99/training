package com.luxoft.atm.domain.model.atm;

import com.luxoft.atm.domain.model.banknote.Banknote;
import com.luxoft.atm.domain.model.banknote.BanknotesBox;
import lombok.Builder;

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
    private final Set<BanknotesBox> banknoteBoxes;
    private final UUID uuid;

    private DefaultATM(Set<BanknotesBox> banknoteBoxes, UUID uuid) {
        this.banknoteBoxes = banknoteBoxes == null ? new HashSet<>() : banknoteBoxes;
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    }

    @Override
    public void take(Banknote banknote) throws ATMBanknotesBoxNotFoundException {
        for (var banknoteBox : banknoteBoxes) {
            if (banknoteBox.getDenomination() == banknote.getDenomination()) {
                banknoteBox.take(banknote);
                return;
            }
        }

        var errorMessagePattern = "A suitable banknote box not found for the \"%s\" banknote denomination";
        throw new ATMBanknotesBoxNotFoundException(String.format(errorMessagePattern, banknote.getDenomination()));
    }

    @Override
    public List<Banknote> give(int sum) throws ATMIncorrectSumException, ATMInsufficientBalanceException {
        if (sum <= 0) {
            throw new ATMIncorrectSumException(String.format("Sum must be greater than 0, but %d given", sum));
        }

        var balance = getBalance();
        if (balance < sum) {
            throw new ATMInsufficientBalanceException("The ATM balance is less than required sum");
        }

        var banknoteBoxesSortedList = banknoteBoxes.stream()
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
    public int getBalance() {
        return banknoteBoxes.stream()
            .map(BanknotesBox::getWorth)
            .reduce(0, Integer::sum);
    }

    @Override
    public String toString() {
        return "DefaultATM{" +
            "uuid=" + uuid +
            '}';
    }
}
