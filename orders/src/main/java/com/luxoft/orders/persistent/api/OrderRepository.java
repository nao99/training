package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * OrderRepository interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface OrderRepository extends CrudRepository<Order, Long>, OrderRepositoryCustom {
    /**
     * Updates an {@link Order} timestamp
     *
     * @param orderId an order id
     */
    @Modifying
    @Query("UPDATE Order SET updatedAt = :timestamp WHERE id = :orderId")
    void updateOrderTimestamp(@Param("orderId") Long orderId, @Param("timestamp") LocalDateTime timestamp);

    /**
     * Finds a count of {@link Order}s
     *
     * @return a count of orders
     */
    @Query("SELECT COUNT(id) FROM Order WHERE done = false")
    long countNonDone();
}
