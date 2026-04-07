package com.wishlist.ai.dto.recommendation;

import java.math.BigDecimal;

public record RecommendationResponse(
        String productId,
        String name,
        String reason,
        String imageUrl,
        BigDecimal price
) {
}
