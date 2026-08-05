package com.combat47.ecommerce.order.application.port.out;

import java.util.UUID;

public interface InventoryPort {

    void reserveStock(UUID productId,  int quantity);

    void releaseStock(UUID productId,  int quantity);

    boolean hasEnoughStock(UUID productId,  int quantity);

}
