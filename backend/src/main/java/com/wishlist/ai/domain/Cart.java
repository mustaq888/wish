package com.wishlist.ai.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "carts")
public class Cart extends BaseEntity {

    private String userId;

    private List<CartItem> items = new ArrayList<>();

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private int totalItems = 0;

    public void addItem(CartItem item) {
        // Check if item already exists
        var existingItem = items.stream()
                .filter(cartItem -> cartItem.getProductId().equals(item.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }

        recalculateTotals();
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        recalculateTotals();
    }

    public void updateItemQuantity(String productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
            return;
        }

        items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        recalculateTotals();
    }

    public void clear() {
        items.clear();
        totalAmount = BigDecimal.ZERO;
        totalItems = 0;
    }

    private void recalculateTotals() {
        totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();
        totalAmount = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}