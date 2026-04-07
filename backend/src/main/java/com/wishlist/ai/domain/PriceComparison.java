package com.wishlist.ai.domain;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "price_comparisons")
public class PriceComparison extends BaseEntity {

    private String platformName;

    private String productUrl;

    private BigDecimal listedPrice;

    private BigDecimal shippingCost = BigDecimal.ZERO;

    private boolean inStock = true;

    private Instant scrapedAt;

    private BigDecimal confidenceScore = BigDecimal.ONE;

    private String productId;
}
