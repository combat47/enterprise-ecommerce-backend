package com.combat47.ecommerce.order.infrastructure.config;

import com.combat47.ecommerce.order.application.port.in.CancelOrderUseCase;
import com.combat47.ecommerce.order.application.port.in.GetOrderUseCase;
import com.combat47.ecommerce.order.application.port.in.ListOrdersUseCase;
import com.combat47.ecommerce.order.application.port.in.PlaceOrderUseCase;
import com.combat47.ecommerce.order.application.port.out.InventoryPort;
import com.combat47.ecommerce.order.application.port.out.OrderRepository;
import com.combat47.ecommerce.order.application.service.CancelOrderService;
import com.combat47.ecommerce.order.application.service.GetOrderService;
import com.combat47.ecommerce.order.application.service.ListOrdersService;
import com.combat47.ecommerce.order.application.service.PlaceOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OrderConfiguration {
    
    @Bean
    public PlaceOrderUseCase placeOrderUseCase(OrderRepository orderRepository,
                                               InventoryPort inventoryPort) {
        return new PlaceOrderService(orderRepository, inventoryPort);
    }
    
    @Bean
    public CancelOrderUseCase cancelOrderUseCase(OrderRepository orderRepository,
                                                 InventoryPort inventoryPort) {
        return new CancelOrderService(orderRepository, inventoryPort);
    }
    
    @Bean
    public GetOrderUseCase  getOrderUseCase(OrderRepository orderRepository) {
        return new GetOrderService(orderRepository);
    }
    
    @Bean
    public ListOrdersUseCase listOrdersUseCase(OrderRepository orderRepository) {
        return new ListOrdersService(orderRepository);
    }
    
}
