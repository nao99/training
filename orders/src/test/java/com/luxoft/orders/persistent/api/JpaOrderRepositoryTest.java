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
 * JpaOrderRepositoryTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
class JpaOrderRepositoryTest extends DatabaseTest {
    private JpaOrderRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new JpaOrderRepository();
    }

    @Test
    void findByIdWhenOrderExists() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        try (var session = sessionFactory.openSession()) {
            createOrder(session, order);

            // when / then
            var selectedOrderOptional = repository.findById(session, order.getId(), LockMode.NONE);

            assertThat(selectedOrderOptional)
                .isPresent();

            assertThat(selectedOrderOptional)
                .containsSame(order);
        }
    }

    @Test
    void findByIdWhenOrderNotExists() throws Exception {
        // given
        try (var session = sessionFactory.openSession()) {
            // when
            var selectedOrderOptional = repository.findById(session, -1L, LockMode.NONE);

            // then
            assertThat(selectedOrderOptional)
                .isEmpty();
        }
    }

    @Test
    void save() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        var orderItem = OrderItem.builder()
            .name("Shoes")
            .order(order)
            .count(10)
            .price(BigDecimal.valueOf(500L))
            .build();

        order.addItem(orderItem);

        try (var session = sessionFactory.openSession()) {
            // when / then
            createOrder(session, order);

            assertThat(order.getId())
                .isNotNull();

            assertThat(orderItem.getId())
                .isNotNull();

            var selectedOrderOptional = repository.findById(session, order.getId(), LockMode.NONE);
            assertThat(selectedOrderOptional)
                .isPresent();

            var selectedOrder = selectedOrderOptional.get();

            assertNotNull(selectedOrder.getId());
            assertEquals(order.getUsername(), selectedOrder.getUsername());
            assertEquals(order.isDone(), selectedOrder.isDone());
            assertEquals(order.getUpdatedAt(), selectedOrder.getUpdatedAt());
        }
    }

    @Test
    void updateOrderTimestamp() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        try (var session = sessionFactory.openSession()) {
            createOrder(session, order);

            var orderUpdatedAt = order.getUpdatedAt();
            session.detach(order);

            // when / then
            var transaction = session.getTransaction();
            transaction.begin();

            repository.updateOrderTimestamp(session, order.getId());
            transaction.commit();

            var selectedOrderOptional = repository.findById(session, order.getId(), LockMode.NONE);

            assertThat(selectedOrderOptional)
                .isPresent();

            assertThat(selectedOrderOptional.get().getUpdatedAt())
                .isAfter(orderUpdatedAt);
        }
    }

    @Test
    void countNonDone() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        try (var session = sessionFactory.openSession()) {
            // when
            var orderCountBeforeAdding = repository.countNonDone(session);
            createOrder(session, order);

            // when
            var orderCountAfterAdding = repository.countNonDone(session);

            // then
            assertThat(orderCountBeforeAdding)
                .isLessThan(orderCountAfterAdding);
        }
    }

    @Test
    void doneAllNonDoneOrdersBatched() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        // when
        try (var session = sessionFactory.openSession()) {
            createOrder(session, order);

            // when
            var transaction = session.getTransaction();
            transaction.begin();

            repository.doneAllNonDoneOrdersBatched(session, 15);
            session.detach(order);

            var selectedOrder = repository.findById(session, order.getId(), LockMode.NONE)
                .orElseThrow();

            transaction.commit();

            // then
            assertThat(selectedOrder.isDone())
                .isTrue();
        }
    }

    private void createOrder(Session session, Order order) {
        var transaction = session.getTransaction();
        transaction.begin();

        repository.save(session, order);
        transaction.commit();
    }
}
