package com.wishlist.ai.service;

import com.wishlist.ai.domain.Product;
import com.wishlist.ai.domain.WishlistItem;
import com.wishlist.ai.dto.recommendation.RecommendationResponse;
import com.wishlist.ai.repository.ProductRepository;
import com.wishlist.ai.repository.WishlistItemRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;

    @Cacheable("recommendations")
    public List<RecommendationResponse> recommendForUser(String userId) {
        var wishlistItems = wishlistItemRepository.findByUserId(userId);
        if (wishlistItems.isEmpty()) {
            return trending();
        }

        Set<String> preferredCategories = wishlistItems.stream()
                .map(item -> productRepository.findById(item.getProductId()).orElse(null))
                .filter(java.util.Objects::nonNull)
                .map(Product::getCategory)
                .collect(java.util.stream.Collectors.toSet());

        Set<String> existingProductIds = wishlistItems.stream()
                .map(WishlistItem::getProductId)
                .collect(java.util.stream.Collectors.toSet());

        return productRepository.findAll().stream()
                .filter(product -> preferredCategories.contains(product.getCategory()))
                .filter(product -> !existingProductIds.contains(product.getId()))
                .sorted(Comparator.comparing(product -> product.getAverageRating(), Comparator.reverseOrder()))
                .limit(5)
                .map(product -> new RecommendationResponse(
                        product.getId(),
                        product.getName(),
                        "Recommended from your wishlist activity in " + product.getCategory(),
                        product.getImageUrl(),
                        product.getBasePrice()))
                .toList();
    }

    public List<RecommendationResponse> trending() {
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(product -> product.getAverageRating(), Comparator.reverseOrder()))
                .limit(6)
                .map(product -> new RecommendationResponse(
                        product.getId(),
                        product.getName(),
                        "Trending based on demand and rating signals",
                        product.getImageUrl(),
                        product.getBasePrice()))
                .toList();
    }
}
