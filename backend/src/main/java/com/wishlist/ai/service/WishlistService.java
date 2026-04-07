package com.wishlist.ai.service;

import com.wishlist.ai.domain.WishlistItem;
import com.wishlist.ai.dto.wishlist.WishlistItemResponse;
import com.wishlist.ai.exception.BadRequestException;
import com.wishlist.ai.exception.ResourceNotFoundException;
import com.wishlist.ai.repository.ProductRepository;
import com.wishlist.ai.repository.UserRepository;
import com.wishlist.ai.repository.WishlistItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PricePredictionService pricePredictionService;

    public List<WishlistItemResponse> getWishlist(String userId) {
        return wishlistItemRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<WishlistItemResponse> addProduct(String userId, String productId) {
        if (wishlistItemRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new BadRequestException("Product already in wishlist");
        }
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        var wishlistItem = new WishlistItem();
        wishlistItem.setUserId(user.getId());
        wishlistItem.setProductId(product.getId());
        wishlistItemRepository.save(wishlistItem);
        log.info("Added product {} to wishlist for user {}", productId, userId);
        return getWishlist(userId);
    }

    public void removeProduct(String userId, String productId) {
        var wishlistItem = wishlistItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));
        wishlistItemRepository.delete(wishlistItem);
        log.info("Removed product {} from wishlist for user {}", productId, userId);
    }

    private WishlistItemResponse toResponse(WishlistItem item) {
        var product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new WishlistItemResponse(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getImageUrl(),
                product.getBasePrice(),
                pricePredictionService.predict(product, wishlistItemRepository.countByProductId(product.getId()))
        );
    }
}
