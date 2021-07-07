package com.luxoft.atm.domain.model.atm.history;

import com.luxoft.atm.domain.model.banknote.Box;
import lombok.Builder;
import lombok.Getter;

/**
 * ATMSnapshot class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-07
 */
@Builder
@Getter
public class ATMSnapshot {
    private final Box box;

    private ATMSnapshot(Box box) {
        this.box = box;
    }
}
