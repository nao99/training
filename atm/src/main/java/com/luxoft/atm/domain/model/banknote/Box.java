package com.luxoft.atm.domain.model.banknote;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Box class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-06
 */
@Getter
@Builder
public class Box implements Serializable {
    private final Set<BanknotesBox> banknoteBoxes;

    private Box(Set<BanknotesBox> banknoteBoxes) {
        this.banknoteBoxes = banknoteBoxes == null ? new HashSet<>() : banknoteBoxes;
    }

    public int size() {
        return banknoteBoxes.size();
    }
}
