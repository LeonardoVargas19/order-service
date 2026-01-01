package org.leoromero.orderservice.services.imp;

import lombok.RequiredArgsConstructor;
import org.leoromero.orderservice.model.Order;
import org.leoromero.orderservice.respositorie.OrderRepository;
import org.leoromero.orderservice.services.OrderServices;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderImplement implements OrderServices {
    private final OrderRepository orderRepository;


    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();

    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No exist order"));

    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        //TODO CREAR LOGICA DE DELETE
    }

    @Override
    public Boolean existOrder(Long id) {
        //TODO CREAR LOGICA DE EXIST
        return null;
    }
}
