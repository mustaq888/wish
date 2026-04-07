package com.wishlist.ai.dto.product;

import java.math.BigDecimal;

public record ReviewDto(
        String userName,
        BigDecimal rating,
        String comment
) {
}
