package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.persistent.DataAccessException;

import java.util.Optional;

/**
 * OrderRepositoryCustom interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-02
 */
public interface OrderRepositoryCustom {
    /**
     * Finds an {@link Order} and locks it
     *
     * @param id an order id
     * @return an order if exists
     */
    Optional<Order> findByIdAndLock(Long id) throws DataAccessException;

    /**
     * Marks as done all non done {@link Order}s
     *
     * @param batchSize a size of batch
     * @throws DataAccessException if something was wrong
     */
    void doneAllNonDoneOrdersBatched(int batchSize) throws DataAccessException;
}
