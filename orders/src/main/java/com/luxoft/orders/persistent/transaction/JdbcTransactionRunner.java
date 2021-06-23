package com.luxoft.orders.persistent.transaction;

import com.luxoft.orders.persistent.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(JdbcTransactionRunner.class);
    private final DataSource dataSource;

    public JdbcTransactionRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T run(TransactionOperation<T> operation) throws DatabaseException {
        return wrapException(() -> {
            try (var connection = dataSource.getConnection()) {
                try {
                    var result = operation.apply(connection);
                    connection.commit();

                    return result;
                } catch (SQLException e) {
                    var errorMessage = String.format("Unable to execute an operation: \"%s\"", e.getMessage());
                    logger.error(errorMessage);

                    connection.rollback();

                    throw new DatabaseException(errorMessage, e);
                }
            }
        });
    }

    /**
     * Wraps passed operation in try catch block
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
            var errorMessage = String.format("Unable to execute an operation: \"%s\"", e.getMessage());
            throw new DatabaseException(errorMessage, e);
        }
    }
}
