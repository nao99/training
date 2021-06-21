package com.luxoft.orders.infrastructure.transaction;

import com.luxoft.orders.infrastructure.DatabaseException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * JdbcTransactionRunner class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
public class JdbcTransactionRunner implements TransactionRunner {
    /**
     * Data source
     */
    private final DataSource dataSource;

    /**
     * JdbcTransactionRunner constructor
     *
     * @param dataSource a data source
     */
    public JdbcTransactionRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T run(TransactionOperation<T> operation) throws DatabaseException {
        return wrapException(() -> {
            try (var connection = dataSource.getConnection()) {
                try {
                    var result = operation.apply(connection);
                    connection.commit();

                    return result;
                } catch (SQLException e) {
                    String errorMessage = String.format("Unable to run an operation: \"%s\"", e.getMessage());
                    connection.rollback();

                    throw new DatabaseException(errorMessage, e);
                }
            }
        });
    }

    /**
     * Wraps passed action in try catch block
     * for convenient exception handling
     *
     * @param operation a callable operation
     *
     * @return an operation result
     * @throws DatabaseException if something was wrong
     */
    private <T> T wrapException(Callable<T> operation) throws DatabaseException {
        try {
            return operation.call();
        } catch (Exception e) {
            String errorMessage = String.format("Unable to get an operation result: \"%s\"", e.getMessage());
            throw new DatabaseException(errorMessage, e);
        }
    }
}
