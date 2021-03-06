package com.luxoft.atm.domain;

import com.luxoft.atm.domain.model.Denomination;
import com.luxoft.atm.domain.model.atm.ATMDisabledException;
import com.luxoft.atm.domain.model.atm.DefaultATM;
import com.luxoft.atm.domain.model.banknote.Banknote;
import com.luxoft.atm.domain.model.banknote.BanknotesBox;
import com.luxoft.atm.domain.model.banknote.Box;
import com.luxoft.atm.domain.model.banknote.DefaultBanknotesBox;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * ATMServiceImplTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-01
 */
public class ATMServiceImplTest {
    @Test
    public void deposit() throws Exception {
        // given
        var service = new ATMServiceImpl();

        var banknotesBox100 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var banknote100 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        Set<BanknotesBox> banknotesBoxes = new HashSet<>() {{
            add(banknotesBox100);
        }};

        var atm = DefaultATM.builder()
            .box(Box.builder().banknoteBoxes(banknotesBoxes).build())
            .enabled(true)
            .build();

        Set<Banknote> banknotes = new HashSet<>() {{
            add(banknote100);
        }};

        // when
        var nonDepositedBanknotes = service.deposit(banknotes, atm);

        // then
        assertThat(nonDepositedBanknotes.size()).isEqualTo(0);
    }

    @Test
    public void depositWhenSomeBanknotesWereGotBack() throws Exception {
        // given
        var service = new ATMServiceImpl();

        var banknotesBox100 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var banknote100 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_100)
            .build();

        var banknote1000 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        Set<BanknotesBox> banknotesBoxes = new HashSet<>() {{
            add(banknotesBox100);
        }};

        var atm = DefaultATM.builder()
            .box(Box.builder().banknoteBoxes(banknotesBoxes).build())
            .enabled(true)
            .build();

        Set<Banknote> banknotes = new HashSet<>() {{
            add(banknote100);
            add(banknote1000);
        }};

        // when
        var nonDepositedBanknotes = service.deposit(banknotes, atm);

        // then
        assertThat(nonDepositedBanknotes.iterator().next()).isSameAs(banknote1000);
    }

    @Test
    void depositWhenATMIsDisabled() throws Exception {
        // given
        var service = new ATMServiceImpl();
        var atm = DefaultATM.builder()
            .enabled(false)
            .build();

        var banknote = Banknote.builder()
            .denomination(Denomination.DENOMINATION_2000)
            .build();

        // when / then
        assertThatThrownBy(() -> service.deposit(Set.of(banknote), atm)).isInstanceOf(ATMDisabledException.class);
    }

    @Test
    void withdrawWhenATMIsDisabled() throws Exception {
        // given
        var service = new ATMServiceImpl();
        var atm = DefaultATM.builder()
            .enabled(false)
            .build();

        // when / then
        assertThatThrownBy(() -> service.withdraw(100, atm)).isInstanceOf(ATMDisabledException.class);
    }

    @Test
    void checkBalanceWhenATMIsDisabled() throws Exception {
        // given
        var service = new ATMServiceImpl();
        var atm = DefaultATM.builder()
            .enabled(false)
            .build();

        // when / then
        assertThatThrownBy(() -> service.checkBalance(atm)).isInstanceOf(ATMDisabledException.class);
    }
}
