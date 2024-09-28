package com.devsuperior.dscommerce;

import com.devsuperior.dscommerce.entities.*;

import java.time.Instant;

public class OrderFactory {

    public static Order createOrder(User user) {
        Order order = new Order(1l, Instant.now(), OrderStatus.WAITING_PAYMENT, user, new Payment());
        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 1, product.getPrice());
        order.getItems().add(orderItem);
        return order;
    }
}
