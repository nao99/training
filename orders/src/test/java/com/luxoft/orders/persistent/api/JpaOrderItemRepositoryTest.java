package com.luxoft.orders.persistent.api;

import com.luxoft.orders.DatabaseTest;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JpaOrderItemRepositoryTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
class JpaOrderItemRepositoryTest extends DatabaseTest {
    private JpaOrderItemRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        repository = new JpaOrderItemRepository();
    }

    @Test
    void findByIdWhenOrderItemExists() throws Exception {
        // given
        try (var session = sessionFactory.openSession()) {
            var order = createOrder(session);
            var orderItem = OrderItem.builder()
                .order(order)
                .name("Shoes")
                .count(10)
                .price(BigDecimal.valueOf(500L))
                .build();

            createOrderItem(session, orderItem);

            // when
            var selectedOrderItemOptional = repository.findById(session, orderItem.getId(), LockMode.NONE);

            // then
            assertThat(selectedOrderItemOptional)
                .isPresent();

            assertThat(selectedOrderItemOptional)
                .containsSame(orderItem);
        }
    }

    @Test
    void findByIdWhenOrderItemNotExists() throws Exception {
        // given
        try (var session = sessionFactory.openSession()) {
            // when
            var selectedOrderItemOptional = repository.findById(session, -1L, LockMode.NONE);

            // then
            assertThat(selectedOrderItemOptional)
                .isEmpty();
        }
    }

    @Test
    void findByIdLockAndAndTryToDelete() throws Exception {
        // given
        try (var session = sessionFactory.openSession()) {
            var order = createOrder(session);
            var orderItem = OrderItem.builder()
                .order(order)
                .name("Shoes")
                .count(10)
                .price(BigDecimal.valueOf(500L))
                .build();

            createOrderItem(session, orderItem);

            // when / then
            var selectedOrderItemOptional = repository.findById(session, orderItem.getId(), LockMode.PESSIMISTIC_READ);

            assertThat(selectedOrderItemOptional)
                .isPresent();

            var query = session.createNativeQuery("DELETE FROM ordering_items WHERE id = ?;");
            query.setParameter(1, orderItem.getId());

            assertThrows(Exception.class, query::executeUpdate);
        }
    }

    @Test
    void save() throws Exception {
        // given
        try (var session = sessionFactory.openSession()) {
            var order = createOrder(session);
            var orderItem = OrderItem.builder()
                .order(order)
                .name("Shoes")
                .count(10)
                .price(BigDecimal.valueOf(500L))
                .build();

            // when / then
            createOrderItem(session, orderItem);
            assertThat(orderItem.getId())
                .isNotNull();

            var selectedOrderItemOptional = repository.findById(session, orderItem.getId(), LockMode.NONE);

            assertThat(selectedOrderItemOptional)
                .isPresent();

            assertThat(selectedOrderItemOptional)
                .containsSame(orderItem);
        }
    }

    /**
     * Creates an {@link Order} for testing purposes
     * This order will be persisted in db
     *
     * @param session a session
     * @return an order
     */
    private Order createOrder(Session session) {
        var transaction = session.getTransaction();
        transaction.begin();

        var order = Order.builder()
            .username("Alex")
            .build();

        session.persist(order);
        session.flush();

        transaction.commit();

        return order;
    }

    private void createOrderItem(Session session, OrderItem orderItem) {
        var transaction = session.getTransaction();
        transaction.begin();

        repository.save(session, orderItem);
        transaction.commit();
    }
}
