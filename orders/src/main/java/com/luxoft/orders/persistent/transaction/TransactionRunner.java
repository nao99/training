package com.luxoft.orders.persistent.transaction;

import com.luxoft.orders.persistent.DatabaseException;

/**
 * TransactionRunner interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-20
 */
public interface TransactionRunner {
    /**
     * Runs passed operation in transaction
     *
     * @param operation an operation that should be executed in a transaction
     *
     * @return an operation result
     * @throws DatabaseException if something was wrong
     */
    <T> T run(TransactionOperation<T> operation) throws DatabaseException;
}
