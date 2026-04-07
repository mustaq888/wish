package com.wishlist.ai.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        String id,
        String name,
        String description,
        String category,
        String brand,
        String imageUrl,
        BigDecimal basePrice,
        BigDecimal averageRating,
        BigDecimal predictedNextPrice,
        List<PriceSnapshotDto> comparisons,
        List<String> aiImages
) {
}
