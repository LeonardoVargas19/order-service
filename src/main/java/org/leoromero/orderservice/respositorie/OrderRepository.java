package org.leoromero.orderservice.respositorie;

import org.leoromero.orderservice.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

}
