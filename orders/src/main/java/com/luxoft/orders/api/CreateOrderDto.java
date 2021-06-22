package com.luxoft.orders.api;

import java.util.Set;

/**
 * CreateOrderDto class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
public class CreateOrderDto {
    private String username;

    private Set<CreateOrderItemDto> items;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<CreateOrderItemDto> getItems() {
        return items;
    }

    public void setItems(Set<CreateOrderItemDto> items) {
        this.items = items;
    }
}
