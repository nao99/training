package com.luxoft.orders.persistent.transaction;

import com.luxoft.orders.persistent.DatabaseException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JpaTransactionRunner class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-19
 */
public class JpaTransactionRunner implements TransactionRunner {
    private static final Logger logger = LoggerFactory.getLogger(JpaTransactionRunner.class);
    private final SessionFactory sessionFactory;

    public JpaTransactionRunner(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <T> T run(TransactionOperation<T> operation) throws DatabaseException {
        try {
            try (var session = sessionFactory.openSession()) {
                var transaction = session.getTransaction();
                try {
                    transaction.begin();
                    var result = operation.apply(session);

                    transaction.commit();

                    return result;
                } catch (HibernateException e) {
                    transaction.rollback();
                    throw e;
                }
            }
        } catch (HibernateException e) {
            var errorMessage = String.format("Unable to execute an operation: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DatabaseException(errorMessage, e);
        }
    }
}
