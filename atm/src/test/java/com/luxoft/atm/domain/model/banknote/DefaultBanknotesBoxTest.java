package com.luxoft.atm.domain.model.banknote;

import com.luxoft.atm.domain.model.Denomination;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DefaultBanknotesBoxTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-30
 */
public class DefaultBanknotesBoxTest {
    @Test
    public void take() throws Exception {
        // given
        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote1 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote2 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        // when
        box.take(banknote1);
        box.take(banknote2);

        // then
        var boxBanknotes = box.getBanknotes();

        assertThat(boxBanknotes.size()).isEqualTo(2);
        assertThat(boxBanknotes.getFirst()).isSameAs(banknote2);
        assertThat(boxBanknotes.getLast()).isSameAs(banknote1);
    }

    @Test
    public void takeWhenDenominationsAreNotTheSame() throws Exception {
        // given
        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote = Banknote.builder()
            .denomination(Denomination.DENOMINATION_50)
            .build();

        // when / then
        assertThatThrownBy(() -> box.take(banknote)).isInstanceOf(BanknoteBoxDenominationException.class);
    }

    @Test
    public void give() throws Exception {
        // given
        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote1 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote2 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        box.take(banknote1);
        box.take(banknote2);

        // when
        var givenBanknote1 = box.give();
        var givenBanknote2 = box.give();

        // then
        assertThat(box.getBanknotes().size()).isEqualTo(0);
        assertThat(givenBanknote1).isSameAs(banknote2);
        assertThat(givenBanknote2).isSameAs(banknote1);
    }

    @Test
    public void giveWhenBoxIsEmpty() throws Exception {
        // given
        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        // when / then
        assertThatThrownBy(box::give).isInstanceOf(BanknoteBoxEmptyException.class);
    }

    @Test
    public void getWorth() throws Exception {
        // given
        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote1 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote2 = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        box.take(banknote1);
        box.take(banknote2);

        // when
        var worth = box.getWorth();

        // then
        assertThat(worth).isEqualTo(2000);
    }

    @Test
    public void getWorthWhenZero() throws Exception {
        // given
        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        // when
        var worth = box.getWorth();

        // then
        assertThat(worth).isEqualTo(0);
    }

    @Test
    public void emptyWhenBoxIsEmpty() throws Exception {
        // given
        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        // when
        var empty = box.empty();

        // then
        assertThat(empty).isTrue();
    }

    @Test
    public void emptyWhenBoxIsNotEmpty() throws Exception {
        // given
        var box = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknote = Banknote.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        box.take(banknote);

        // when
        var empty = box.empty();

        // then
        assertThat(empty).isFalse();
    }
}
