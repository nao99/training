package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;

/**
 * DoneOrdersService interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface DoneOrdersService {
    /**
     * Marks all {@link Order}s as done
     */
    void doneAll();
}
