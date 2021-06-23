package com.luxoft.orders.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
public class Order {
    private final Long id;
    private final String username;
    private boolean done;
    private LocalDateTime updatedAt;
    private final List<OrderItem> items;

    private Order(Long id, String username, boolean done, LocalDateTime updatedAt, List<OrderItem> items) {
        this.id = id;
        this.username = username;
        this.done = done;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    public static Order of(Long id, String username, boolean done, LocalDateTime updatedAt, List<OrderItem> items) {
        return new Order(id, username, done, updatedAt, items);
    }

    public static Order of(String username) {
        return new Order(null, username, false, LocalDateTime.now(), new ArrayList<>());
    }

    public Order withId(Long id) {
        return new Order(id, username, done, updatedAt, items);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isDone() {
        return done;
    }

    public void done() {
        done = true;
        updateTimestamp();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void updateTimestamp() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", done=" + done +
            ", updatedAt=" + updatedAt +
            ", items=" + items +
            '}';
    }
}
