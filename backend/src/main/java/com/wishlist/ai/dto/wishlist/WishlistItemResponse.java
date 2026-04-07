package com.wishlist.ai.dto.wishlist;

import java.math.BigDecimal;

public record WishlistItemResponse(
        String wishlistItemId,
        String productId,
        String name,
        String brand,
        String category,
        String imageUrl,
        BigDecimal price,
        BigDecimal predictedNextPrice
) {
}
