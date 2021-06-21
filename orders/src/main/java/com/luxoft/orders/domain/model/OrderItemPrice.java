package com.luxoft.orders.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * OrderItemPrice class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class OrderItemPrice {
    /**
     * Price
     */
    private final BigDecimal price;

    /**
     * OrderItemPrice constructor
     *
     * @param price a price
     * @throws IllegalArgumentException if price less than or equal to 0
     */
    private OrderItemPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            String errorMessage = String.format("Order item price must be greater than 0, but %s given", price);
            throw new IllegalArgumentException(errorMessage);
        }

        this.price = price;
    }

    /**
     * OrderItemPrice static constructor
     *
     * @param price a price
     */
    public static OrderItemPrice of(BigDecimal price) {
        return new OrderItemPrice(price);
    }

    public BigDecimal price() {
        return price;
    }

    public OrderItemPrice divide(long divisor) {
        BigDecimal dividedPriceValue = price.divide(BigDecimal.valueOf(divisor), RoundingMode.DOWN);
        return new OrderItemPrice(dividedPriceValue);
    }

    public OrderItemPrice multiply(long multiplicand) {
        BigDecimal multipliedPriceValue = price.multiply(BigDecimal.valueOf(multiplicand));
        return new OrderItemPrice(multipliedPriceValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (!other.getClass().isAssignableFrom(OrderItemPrice.class)) {
            return false;
        }

        OrderItemPrice otherOrderItemPrice = (OrderItemPrice) other;

        return price.equals(otherOrderItemPrice.price);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * 17 + price.hashCode();
    }
}
