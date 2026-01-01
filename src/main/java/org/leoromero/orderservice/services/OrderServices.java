package org.leoromero.orderservice.services;

import org.leoromero.orderservice.model.Order;

import java.util.List;

public interface OrderServices {
    List<Order> getAllOrder();

    Order findById(Long id);

    Order createOrder(Order order);

    Boolean existOrder(Long id);

    void deleteOrder(Long id);


}
