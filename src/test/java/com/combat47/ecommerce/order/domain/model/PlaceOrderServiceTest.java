package com.combat47.ecommerce.order.domain.model;

import com.combat47.ecommerce.order.application.command.PlaceOrderCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.combat47.ecommerce.order.application.port.out.InventoryPort;
import com.combat47.ecommerce.order.application.port.out.OrderRepository;
import com.combat47.ecommerce.order.application.service.PlaceOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceOrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private InventoryPort  inventoryPort;
    @InjectMocks private PlaceOrderService service;

    @Test
    void should_place_order_successfully() {
        var command = new PlaceOrderCommand(
                UUID.randomUUID(),
                List.of(new PlaceOrderCommand.OrderItemCommand(
                        UUID.randomUUID(),
                        "Product",
                        new BigDecimal("99.99"),
                        2
                ))
        );

        when(inventoryPort.hasEnoughStock(any(), anyInt())).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        OrderResponse response = service.placeOrder(command);

        assertNotNull(response);
        assertEquals(OrderStatus.PENDING.name(), response.status());
        verify(inventoryPort, times(1)).reserveStock(any(), anyInt());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

}
