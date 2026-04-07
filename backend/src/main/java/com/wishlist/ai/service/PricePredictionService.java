package com.wishlist.ai.service;

import com.wishlist.ai.domain.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class PricePredictionService {

    public BigDecimal predict(Product product, int wishlistDemandScore) {
        var demandFactor = BigDecimal.valueOf(Math.min(wishlistDemandScore, 10L)).multiply(BigDecimal.valueOf(0.003));
        var ratingFactor = product.getAverageRating().multiply(BigDecimal.valueOf(0.01));
        var multiplier = BigDecimal.ONE.add(demandFactor).subtract(ratingFactor.min(BigDecimal.valueOf(0.03)));
        return product.getBasePrice().multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}
