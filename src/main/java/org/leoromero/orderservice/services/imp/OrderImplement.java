package org.leoromero.orderservice.services.imp;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.leoromero.orderservice.model.Order;
import org.leoromero.orderservice.model.OrderLineItems;
import org.leoromero.orderservice.model.dto.InventoryResponse;
import org.leoromero.orderservice.respositorie.OrderRepository;
import org.leoromero.orderservice.services.OrderServices;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderImplement implements OrderServices {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;


    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();

    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No exist order"));

    }

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackApp")
    @Override
    public Order createOrder(Order order) {
        List<String> skuCoder = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        /*

          aquí llamo a al servicio produc que a su ves me devuelve los datos de la BD
         */


        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://product-services/api/product/stock",
                        uriBuilder -> uriBuilder
                                .queryParam("skuCode", skuCoder).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


        if (inventoryResponses.length == 0) {
            throw new IllegalArgumentException("El producto no existe en el inventario");
        }


        if (inventoryResponses.length != skuCoder.size()) {
            throw new IllegalArgumentException("Algunos productos no fueron encontrados");
        }
        for (OrderLineItems items : order.getOrderLineItems()) {
            Arrays.stream(inventoryResponses)
                    .filter(sku -> items.getSkuCode().equals(sku.getSku()))
                    .findFirst()
                    .ifPresent(element -> items.setPrice(element.getPrices()));


        }

        boolean allProductStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::getIsInStock);

        if (allProductStock) {
            return orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("El producto no esta en stock, intente mas tarde");
        }


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

    public Order fallbackApp(Order order, Throwable throwable) {
        throw new RuntimeException("Oops! El servicio de inventario no responde. Intenta más tarde.");
    }


}
