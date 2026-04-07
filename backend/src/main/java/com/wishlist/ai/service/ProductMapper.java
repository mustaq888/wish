package com.wishlist.ai.service;

import com.wishlist.ai.domain.PriceComparison;
import com.wishlist.ai.domain.Product;
import com.wishlist.ai.domain.ProductImage;
import com.wishlist.ai.dto.product.PriceSnapshotDto;
import com.wishlist.ai.dto.product.ProductResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product, BigDecimal predictedPrice, List<PriceComparison> comparisons, List<ProductImage> aiImages) {
        var priceSnapshots = comparisons.stream()
                .map(this::toPriceSnapshot)
                .sorted((left, right) -> left.totalPrice().compareTo(right.totalPrice()))
                .toList();
        var imageUrls = aiImages.stream()
                .map(image -> image.getImageUrl())
                .toList();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getBrand(),
                product.getImageUrl(),
                product.getBasePrice(),
                product.getAverageRating(),
                predictedPrice,
                priceSnapshots,
                imageUrls
        );
    }

    public PriceSnapshotDto toPriceSnapshot(PriceComparison comparison) {
        var totalPrice = comparison.getListedPrice().add(comparison.getShippingCost());
        return new PriceSnapshotDto(
                comparison.getId(),
                comparison.getPlatformName(),
                comparison.getProductUrl(),
                comparison.getListedPrice(),
                comparison.getShippingCost(),
                totalPrice,
                comparison.isInStock(),
                comparison.getConfidenceScore()
        );
    }
}
