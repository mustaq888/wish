package com.wishlist.ai.dto.cart;

import com.wishlist.ai.domain.CartItem;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        String id,
        String userId,
        List<CartItem> items,
        BigDecimal totalAmount,
        int totalItems
) {}