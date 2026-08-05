package com.combat47.ecommerce.order.api.controller;

import com.combat47.ecommerce.order.application.command.CancelOrderCommand;
import com.combat47.ecommerce.order.application.command.GetOrderQuery;
import com.combat47.ecommerce.order.application.command.PlaceOrderCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.combat47.ecommerce.order.application.port.in.CancelOrderUseCase;
import com.combat47.ecommerce.order.application.port.in.GetOrderUseCase;
import com.combat47.ecommerce.order.application.port.in.ListOrdersUseCase;
import com.combat47.ecommerce.order.application.port.in.PlaceOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final PlaceOrderUseCase placeOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase, CancelOrderUseCase cancelOrderUseCase, GetOrderUseCase getOrderUseCase, ListOrdersUseCase listOrdersUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderCommand command,
                                                    Principal principal) {
        OrderResponse response = placeOrderUseCase.placeOrder(command);
        return ResponseEntity.status(201).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        GetOrderQuery query = new GetOrderQuery(id, userId);
        return ResponseEntity.ok(getOrderUseCase.getOrder(query));
    }
    
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return ResponseEntity.ok(listOrdersUseCase.listOrders(userId));
    }
    
    @DeleteMapping
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID id, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return ResponseEntity.ok(cancelOrderUseCase.cancelOrder(new CancelOrderCommand(id, userId)));
    }
}
