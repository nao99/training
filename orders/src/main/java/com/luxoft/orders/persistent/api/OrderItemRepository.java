package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.OrderItem;
import org.springframework.data.repository.CrudRepository;

/**
 * OrderItemRepository interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface OrderItemRepository extends CrudRepository<OrderItem, Long>, OrderItemRepositoryCustom {
}
