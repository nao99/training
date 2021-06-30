package com.luxoft.atm.domain.model.atm;

import com.luxoft.atm.domain.model.Denomination;
import com.luxoft.atm.domain.model.banknote.Banknote;
import com.luxoft.atm.domain.model.banknote.BanknotesBox;
import com.luxoft.atm.domain.model.banknote.DefaultBanknotesBox;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DefaultATMTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public class DefaultATMTest {
    @Test
    public void take() throws Exception {
        // given
        var banknotesBox100 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var banknotesBox500 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_500)
            .build();

        var banknotesBox1000 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknotesBox2000 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_2000)
            .build();

        var banknote100 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var banknote1000 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote2000 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_2000)
            .build();

        Set<BanknotesBox> banknotesBoxes = new HashSet<>() {{
            add(banknotesBox100);
            add(banknotesBox500);
            add(banknotesBox1000);
            add(banknotesBox2000);
        }};

        var atm = DefaultATM.builder()
            .banknoteBoxes(banknotesBoxes)
            .build();

        // when
        atm.take(banknote100);
        atm.take(banknote1000);
        atm.take(banknote2000);

        // then
        assertThat(banknotesBox100.give()).isSameAs(banknote100);
        assertThat(banknotesBox500.getBanknotes().size()).isEqualTo(0);
        assertThat(banknotesBox1000.give()).isSameAs(banknote1000);
        assertThat(banknotesBox2000.getBanknotes().size()).isEqualTo(1);
        assertThat(banknotesBox2000.getBanknotes().iterator().next()).isSameAs(banknote2000);
    }

    @Test
    public void takeWhenBanknotesBoxNotFoundForBanknotes() throws Exception {
        // given
        var banknote100 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var atm = DefaultATM.builder()
            .build();

        // when / then
        assertThatThrownBy(() -> atm.take(banknote100)).isInstanceOf(ATMBanknotesBoxNotFoundException.class);
    }

    @Test
    public void give() throws Exception {
        // given
        var banknotesBox500 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_500)
            .build();

        var banknotesBox2000 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_2000)
            .build();

        var banknotesBox5000 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_5000)
            .build();

        for (int i = 0; i < 4; i++) {
            var banknote500 = Banknote.builder()
                .denomination(Denomination.DENOMINATION_500)
                .build();

            banknotesBox500.take(banknote500);
        }

        for (int i = 0; i < 3; i++) {
            var banknote2000 = Banknote.builder()
                .denomination(Denomination.DENOMINATION_2000)
                .build();

            banknotesBox2000.take(banknote2000);
        }

        var banknote5000 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_5000)
            .build();

        banknotesBox5000.take(banknote5000);

        Set<BanknotesBox> banknotesBoxes = new HashSet<>() {{
            add(banknotesBox500);
            add(banknotesBox2000);
            add(banknotesBox5000);
        }};

        var atm = DefaultATM.builder()
            .banknoteBoxes(banknotesBoxes)
            .build();

        // when
        var banknotes1 = atm.give(500);
        var banknotes2 = atm.give(1000);
        var banknotes3 = atm.give(4500);
        var banknotes4 = atm.give(7000);

        // then
        assertThat(banknotes1.size()).isEqualTo(1);
        assertThat(banknotes1.get(0).getDenomination()).isEqualTo(Denomination.DENOMINATION_500);

        assertThat(banknotes2.size()).isEqualTo(2);
        assertThat(banknotes2.get(0).getDenomination()).isEqualTo(Denomination.DENOMINATION_500);
        assertThat(banknotes2.get(1).getDenomination()).isEqualTo(Denomination.DENOMINATION_500);

        assertThat(banknotes3.size()).isEqualTo(3);
        assertThat(banknotes3.get(0).getDenomination()).isEqualTo(Denomination.DENOMINATION_2000);
        assertThat(banknotes3.get(1).getDenomination()).isEqualTo(Denomination.DENOMINATION_2000);
        assertThat(banknotes3.get(2).getDenomination()).isEqualTo(Denomination.DENOMINATION_500);

        assertThat(banknotes4.size()).isEqualTo(2);
        assertThat(banknotes4.get(0).getDenomination()).isEqualTo(Denomination.DENOMINATION_5000);
        assertThat(banknotes4.get(1).getDenomination()).isEqualTo(Denomination.DENOMINATION_2000);
    }

    @Test
    public void giveWhenSumIsNegative() throws Exception {
        // given
        var atm = DefaultATM.builder()
            .build();

        // when / then
        assertThatThrownBy(() -> atm.give(-10)).isInstanceOf(ATMIncorrectSumException.class);
    }

    @Test
    public void giveWhenSumIsZero() throws Exception {
        // given
        var atm = DefaultATM.builder()
            .build();

        // when / then
        assertThatThrownBy(() -> atm.give(0)).isInstanceOf(ATMIncorrectSumException.class);
    }

    @Test
    public void giveWhenSumCannotBeDenominated() throws Exception {
        // given
        var banknote1 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_50)
            .build();

        var banknote2 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_50)
            .build();

        Deque<Banknote> banknotes = new ArrayDeque<>() {{
            add(banknote1);
            add(banknote2);
        }};

        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_50)
            .banknotes(banknotes)
            .build();

        Set<BanknotesBox> banknoteBoxes = new HashSet<>() {{
            add(box);
        }};

        var atm = DefaultATM.builder()
            .banknoteBoxes(banknoteBoxes)
            .build();

        // when / then
        assertThatThrownBy(() -> atm.give(51)).isInstanceOf(ATMIncorrectSumException.class);
        assertThat(box.getBanknotes().size()).isEqualTo(2);
    }

    @Test
    public void giveWhenBanknotesCouldBeGiven() throws Exception {
        // given
        var banknote1 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_50)
            .build();

        var banknote2 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_50)
            .build();

        var banknote3 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_50)
            .build();

        var banknote4 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var banknote5 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        Deque<Banknote> banknotes1 = new ArrayDeque<>() {{
            add(banknote1);
            add(banknote2);
            add(banknote3);
        }};

        Deque<Banknote> banknotes2 = new ArrayDeque<>() {{
            add(banknote4);
            add(banknote5);
        }};

        var box1 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_50)
            .banknotes(banknotes1)
            .build();

        var box2 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_100)
            .banknotes(banknotes2)
            .build();

        Set<BanknotesBox> banknoteBoxes = new HashSet<>() {{
            add(box1);
            add(box2);
        }};

        var atm = DefaultATM.builder()
            .banknoteBoxes(banknoteBoxes)
            .build();

        // when
        var banknotes = atm.give(200);

        // then
        assertThat(banknotes.size()).isEqualTo(2);
        assertThat(banknotes.get(0).getDenomination()).isEqualTo(Denomination.DENOMINATION_100);
        assertThat(banknotes.get(1).getDenomination()).isEqualTo(Denomination.DENOMINATION_100);
    }

    @Test
    public void giveWhenBalanceIsNotEnough() throws Exception {
        // given
        var atm = DefaultATM.builder()
            .build();

        // when / then
        assertThatThrownBy(() -> atm.give(100)).isInstanceOf(ATMInsufficientBalanceException.class);
    }

    @Test
    public void getBalance() throws Exception {
        // given
        var banknotesBox100 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var banknotesBox500 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_500)
            .build();

        var banknotesBox1000 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote100 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var banknote1000 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        Set<BanknotesBox> banknotesBoxes = new HashSet<>() {{
            add(banknotesBox100);
            add(banknotesBox500);
            add(banknotesBox1000);
        }};

        var atm = DefaultATM.builder()
            .banknoteBoxes(banknotesBoxes)
            .build();

        atm.take(banknote100);
        atm.take(banknote1000);

        // when
        var balance = atm.getBalance();

        // then
        assertThat(balance).isEqualTo(1100);
    }

    @Test
    public void getBalanceWhenBalanceIsZero() throws Exception {
        // given
        var atm = DefaultATM.builder()
            .build();

        // when
        var balance = atm.getBalance();

        // then
        assertThat(balance).isEqualTo(0);
    }
}
