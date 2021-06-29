package com.luxoft.orders.domain.model;

import lombok.Builder;
import lombok.Getter;

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
@Getter
@Builder
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
        this.updatedAt = updatedAt == null ? LocalDateTime.now() : updatedAt;
        this.items = items == null ? new ArrayList<>() : items;
    }

    public Order withId(Long id) {
        return new Order(id, username, done, updatedAt, items);
    }

    public void done() {
        done = true;
        updateTimestamp();
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
