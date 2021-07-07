package com.luxoft.atm.domain.model.atm.history;

import lombok.Builder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * ATMHistory class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-07
 */
@Builder
public class ATMHistory {
    private final Deque<ATMSnapshot> snapshots;

    private ATMHistory(Deque<ATMSnapshot> snapshots) {
        this.snapshots = snapshots == null ? new ArrayDeque<>() : snapshots;
    }

    public void push(ATMSnapshot snapshot) {
        snapshots.push(snapshot);
    }

    public ATMSnapshot pop() throws ATMHistoryEmptyException {
        try {
            return snapshots.pop();
        } catch (NoSuchElementException e) {
            throw new ATMHistoryEmptyException("ATM History is empty now", e);
        }
    }

    public int size() {
        return snapshots.size();
    }
}
