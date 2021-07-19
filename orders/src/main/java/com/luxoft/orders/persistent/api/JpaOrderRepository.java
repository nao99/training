package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.persistent.DataAccessException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * JpaOrderRepository class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-19
 */
public class JpaOrderRepository implements OrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(JpaOrderRepository.class);

    @Override
    public Optional<Order> findById(Session session, Long id, LockMode lockMode) {
        Order order = session.get(Order.class, id, lockMode);
        return Optional.ofNullable(order);
    }

    @Override
    public void save(Session session, Order order) {
        session.save(order);
    }

    @Override
    public void updateOrderTimestamp(Session session, Long id) throws DataAccessException {
        var now = LocalDateTime.now();
        var query = session.createNativeQuery("UPDATE ordering SET updated_at = ? WHERE id = ?;");

        query.setParameter(1, now);
        query.setParameter(2, id);

        try {
            query.executeUpdate();
        } catch (PersistenceException e) {
            var errorMessage = String.format("Unable to update a %d order timestamp: \"%s\"", id, e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }

    @Override
    public long countNonDone(Session session) throws DataAccessException {
        var query = session.createNativeQuery("SELECT COUNT(id) FROM ordering WHERE done = false;");
        try {
            var result = (BigInteger) query.getSingleResult();
            return result.longValue();
        } catch (PersistenceException e) {
            var errorMessage = String.format("Unable to find a non done orders count: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }

    @Override
    public void doneAllNonDoneOrdersBatched(Session session, int batchSize) throws DataAccessException {
        var sql =
            "WITH non_done_orders AS (" +
                "SELECT id " +
                "FROM ordering " +
                "WHERE done = false " +
                    "FOR UPDATE " +
                        "SKIP LOCKED " +
                    "LIMIT ?" +
                ")" +
            "UPDATE ordering " +
            "SET done = true, updated_at = NOW() " +
            "FROM non_done_orders " +
            "WHERE ordering.id = non_done_orders.id;";

        var query = session.createNativeQuery(sql);
        query.setParameter(1, batchSize);

        try {
            query.executeUpdate();
        } catch (PersistenceException e) {
            var errorMessage = String.format("Unable to done non done orders: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }
}
