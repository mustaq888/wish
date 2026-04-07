package com.wishlist.ai.dto.compare;

import java.math.BigDecimal;

public record PlatformPriceDto(
        String platformName,
        BigDecimal listedPrice,
        BigDecimal shippingCost,
        BigDecimal totalPrice,
        boolean inStock,
        String productUrl
) {
}
