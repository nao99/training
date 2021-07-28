package com.luxoft.orders.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * OrderItem class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
@Entity
@Table(name = "ordering_items")
@Data
@Builder
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordering_id", nullable = false)
    private Order order;

    @Column(name = "item_name", length = 200, nullable = false)
    private String name;

    @Column(name = "item_count", nullable = false)
    private int count;

    @Column(name = "item_price", nullable = false)
    private BigDecimal price;

    private OrderItem(Long id, Order order, String name, int count, BigDecimal price) {
        this.id = id;
        this.order = order;
        this.name = name;
        this.count = count;
        this.price = price;
    }

    /**
     * Changes a count of this item
     *
     * @param newCount a changeable count
     */
    public void changeCount(int newCount) {
        count = newCount;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + id +
            ", orderId=" + (order == null ? "null" : order.getId()) +
            ", name='" + name + '\'' +
            ", count=" + count +
            ", price=" + price +
            '}';
    }
}
