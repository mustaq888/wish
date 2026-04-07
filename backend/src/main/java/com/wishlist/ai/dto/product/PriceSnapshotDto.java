package com.wishlist.ai.dto.product;

import java.math.BigDecimal;

public record PriceSnapshotDto(
        String id,
        String platformName,
        String productUrl,
        BigDecimal listedPrice,
        BigDecimal shippingCost,
        BigDecimal totalPrice,
        boolean inStock,
        BigDecimal confidenceScore
) {
}
