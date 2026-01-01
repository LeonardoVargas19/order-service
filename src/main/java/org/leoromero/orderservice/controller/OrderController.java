package org.leoromero.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.leoromero.orderservice.model.Order;
import org.leoromero.orderservice.respositorie.OrderRepository;
import org.leoromero.orderservice.services.OrderServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServices orderServices;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        order.setOrderNumber(UUID.randomUUID().toString());
        return new ResponseEntity<>(orderServices.createOrder(order), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return new ResponseEntity<>(orderServices.getAllOrder(), HttpStatus.OK);
    }


}
