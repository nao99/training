package com.luxoft.orders.persistent.transaction;

import com.luxoft.orders.persistent.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

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
        try {
            try (var connection = dataSource.getConnection()) {
                try {
                    var result = operation.apply(connection);
                    connection.commit();

                    return result;
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }
            }
        } catch (SQLException e) {
            var errorMessage = String.format("Unable to execute an operation: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DatabaseException(errorMessage, e);
        }
    }
}
