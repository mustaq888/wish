package com.wishlist.ai.dto.compare;

import java.math.BigDecimal;
import java.util.List;

public record ComparisonResponse(
        String productId,
        String productName,
        BigDecimal predictedNextPrice,
        List<PlatformPriceDto> platforms
) {
}
