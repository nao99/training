package com.luxoft.orders.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Entity
@Table(name = "ordering")
@Data
@Builder
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 200, nullable = false)
    private String username;

    @Column(name = "done", nullable = false)
    private boolean done;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    private Order(Long id, String username, boolean done, LocalDateTime updatedAt, List<OrderItem> items) {
        this.id = id;
        this.username = username;
        this.done = done;
        this.updatedAt = updatedAt == null ? LocalDateTime.now() : updatedAt;
        this.items = items == null ? new ArrayList<>() : items;
    }

    public void done() {
        done = true;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    @PrePersist
    @PreUpdate
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
