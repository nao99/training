package com.luxoft.orders.domain.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Order class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
public class Order {
    /**
     * Id
     */
    private final OrderId id;

    /**
     * Username
     */
    private final OrderUsername username;

    /**
     * Done flag
     */
    private boolean done;

    /**
     * Updated at field
     */
    private Timestamp updatedAt;

    /**
     * Items
     */
    private final Set<OrderItem> items;

    /**
     * Order constructor
     *
     * @param id        an id
     * @param username  a username
     * @param done      a done flag
     * @param updatedAt an updated at
     * @param items     an items set
     */
    private Order(OrderId id, OrderUsername username, boolean done, Timestamp updatedAt, Set<OrderItem> items) {
        this.id = id;
        this.username = username;
        this.done = done;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    /**
     * Order static constructor
     *
     * @param orderId   an id
     * @param username  a username
     * @param done      a done flag
     * @param updatedAt an updated at
     *
     * @return an order with id
     */
    public static Order of(OrderId orderId, OrderUsername username, boolean done, Timestamp updatedAt) {
        return new Order(orderId, username, done, updatedAt, new HashSet<>());
    }

    /**
     * Order static constructor
     *
     * @param username a username
     * @return a new order (without id)
     */
    public static Order of(OrderUsername username) {
        return new Order(null, username, false, Timestamp.from(Instant.now()), new HashSet<>());
    }

    /**
     * Recreates this order with id
     *
     * @param id an id
     * @return this order with id
     */
    public Order withId(OrderId id) {
        return new Order(id, username, done, updatedAt, items);
    }

    public OrderId getId() {
        return id;
    }

    public OrderUsername getUsername() {
        return username;
    }

    public boolean isDone() {
        return done;
    }

    public void done() {
        done = true;
        updateTimestamp();
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    private void updateTimestamp() {
        updatedAt = Timestamp.from(Instant.now());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (!other.getClass().isAssignableFrom(Order.class)) {
            return false;
        }

        Order otherOrder = (Order) other;

        return Objects.equals(this, otherOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 17;

        if (id != null) {
            hash = 31 * hash + id.hashCode();
        }

        hash = 31 * hash + username.hashCode();
        hash = 31 * hash + Boolean.hashCode(done);
        hash = 31 * hash + items.hashCode();

        return hash;
    }
}
