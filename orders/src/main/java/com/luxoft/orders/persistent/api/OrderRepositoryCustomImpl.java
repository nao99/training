package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.persistent.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import java.util.Optional;

/**
 * OrderRepositoryCustomImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-19
 */
@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(OrderRepositoryCustomImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public OrderRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Order> findByIdAndLock(Long id) throws DataAccessException {
        return Optional.ofNullable(entityManager.find(Order.class, id, LockModeType.PESSIMISTIC_READ));
    }

    @Override
    public void doneAllNonDoneOrdersBatched(int batchSize) throws DataAccessException {
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

        var query = entityManager.createNativeQuery(sql);
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
