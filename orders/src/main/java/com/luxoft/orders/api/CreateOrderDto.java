package com.luxoft.orders.api;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * CreateOrderDto class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
@Validated
public class CreateOrderDto {
    @NotNull
    private String username;

    @NotEmpty
    @Valid
    private List<CreateOrderItemDto> items;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CreateOrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<CreateOrderItemDto> items) {
        this.items = items;
    }
}
