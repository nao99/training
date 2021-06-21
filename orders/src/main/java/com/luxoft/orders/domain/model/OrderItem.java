package com.luxoft.orders.domain.model;

/**
 * OrderItem class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
public class OrderItem {
    /**
     * Item id
     */
    private final OrderItemId id;

    /**
     * Order id
     */
    private final OrderId orderId;

    /**
     * Item name
     */
    private final OrderItemName itemName;

    /**
     * Items count
     */
    private OrderItemCount count;

    /**
     * Item price
     */
    private OrderItemPrice price;

    /**
     * OrderItem constructor
     *
     * @param id       an item id
     * @param orderId  an order id
     * @param itemName an item id
     * @param count    an item count
     * @param price    an item price
     */
    private OrderItem(OrderItemId id, OrderId orderId, OrderItemName itemName, OrderItemCount count, OrderItemPrice price) {
        this.id = id;
        this.orderId = orderId;
        this.itemName = itemName;
        this.count = count;
        this.price = price;
    }

    /**
     * OrderItem static constructor
     *
     * @param id       an id
     * @param orderId  an order id
     * @param itemName a name
     * @param count    a count
     * @param price    a price
     */
    public static OrderItem of(
        OrderItemId id,
        OrderId orderId,
        OrderItemName itemName,
        OrderItemCount count,
        OrderItemPrice price
    ) {
        return new OrderItem(id, orderId, itemName, count, price);
    }

    /**
     * OrderItem static constructor
     *
     * @param orderId  an order id
     * @param itemName an item id
     * @param count    an item count
     * @param price    an item price
     */
    public static OrderItem of(OrderId orderId, OrderItemName itemName, OrderItemCount count, OrderItemPrice price) {
        return new OrderItem(null, orderId, itemName, count, price);
    }

    /**
     * Recreates this order item with id
     *
     * @param id an id
     * @return this order item with id
     */
    public OrderItem withId(OrderItemId id) {
        return new OrderItem(id, orderId, itemName, count, price);
    }

    public OrderItemId getId() {
        return id;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderItemName getName() {
        return itemName;
    }

    public OrderItemCount getCount() {
        return count;
    }

    public void replaceCount(int countValue) {
        OrderItemCount replacedCount = OrderItemCount.of(countValue);
        if (count.equals(replacedCount)) {
            return;
        }

        OrderItemPrice pricePerOneItem = price.divide(count.count());
        OrderItemPrice replacedPrice = pricePerOneItem.multiply(replacedCount.count());

        count = replacedCount;
        price = replacedPrice;
    }

    public OrderItemPrice getPrice() {
        return price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (!other.getClass().isAssignableFrom(OrderItem.class)) {
            return false;
        }

        OrderItem otherOrderItem = (OrderItem) other;

        return orderId.equals(otherOrderItem.orderId)
            && itemName.equals(otherOrderItem.itemName)
            && count.equals(otherOrderItem.count)
            && price.equals(otherOrderItem.price);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 17;

        hash = 31 * hash + orderId.hashCode();
        hash = 31 * hash + itemName.hashCode();
        hash = 31 * hash + count.hashCode();
        hash = 31 * hash + price.hashCode();

        return hash;
    }
}
