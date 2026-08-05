package com.combat47.ecommerce.order.infrastructure.persistence.adapter;

import com.combat47.ecommerce.order.application.port.out.InventoryPort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class InMemoryInventoryAdapter implements InventoryPort {

    private final Map<UUID, Integer> stock = new ConcurrentHashMap<>();

    @Override
    public void reserveStock(UUID productId, int quantity) {
        stock.compute(productId, (k, v) -> v == null ? -quantity : v - quantity);
    }

    @Override
    public void releaseStock(UUID productId, int quantity) {
        stock.compute(productId, (k, v) -> v == null ? quantity : v + quantity);
    }

    @Override
    public boolean hasEnoughStock(UUID productId, int quantity) {
        return stock.getOrDefault(productId, 0) >= quantity;
    }

    public void setStock(UUID productId, int quantity) {
        stock.put(productId, quantity);
    }
}
