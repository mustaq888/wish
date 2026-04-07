package com.wishlist.ai.service;

import com.wishlist.ai.dto.compare.ComparisonResponse;
import com.wishlist.ai.dto.compare.PlatformPriceDto;
import com.wishlist.ai.exception.ResourceNotFoundException;
import com.wishlist.ai.repository.PriceComparisonRepository;
import com.wishlist.ai.repository.ProductRepository;
import com.wishlist.ai.repository.WishlistItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComparisonService {

    private final ProductRepository productRepository;
    private final PriceComparisonRepository priceComparisonRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final PricePredictionService pricePredictionService;

    public ComparisonResponse compare(String productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        var platforms = priceComparisonRepository.findByProductIdOrderByListedPriceAsc(productId)
                .stream()
                .map(comparison -> new PlatformPriceDto(
                        comparison.getPlatformName(),
                        comparison.getListedPrice(),
                        comparison.getShippingCost(),
                        comparison.getListedPrice().add(comparison.getShippingCost()),
                        comparison.isInStock(),
                        comparison.getProductUrl()))
                .toList();
        return new ComparisonResponse(
                product.getId(),
                product.getName(),
                pricePredictionService.predict(product, wishlistItemRepository.countByProductId(productId)),
                platforms
        );
    }

    public List<ComparisonResponse> compareMany(List<String> productIds) {
        return productIds.stream().map(this::compare).toList();
    }
}
