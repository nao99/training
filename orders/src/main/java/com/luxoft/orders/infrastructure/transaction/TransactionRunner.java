package com.luxoft.orders.infrastructure.transaction;

import com.luxoft.orders.infrastructure.DatabaseException;

/**
 * TransactionRunner interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-20
 */
public interface TransactionRunner {
    /**
     * Runs passed transaction
     *
     * @param operation an operation that should be executed in a transaction
     *
     * @return an operation result
     * @throws DatabaseException if something was wrong on a db side
     */
    <T> T run(TransactionOperation<T> operation) throws DatabaseException;
}
