package com.wishlist.ai.service;

import com.wishlist.ai.domain.Cart;
import com.wishlist.ai.domain.CartItem;
import com.wishlist.ai.domain.Product;
import com.wishlist.ai.domain.User;
import com.wishlist.ai.dto.cart.CartItemRequest;
import com.wishlist.ai.dto.cart.CartResponse;
import com.wishlist.ai.exception.ResourceNotFoundException;
import com.wishlist.ai.repository.CartRepository;
import com.wishlist.ai.repository.ProductRepository;
import com.wishlist.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartResponse getCart(String userId) {
        validateUser(userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));

        return mapToCartResponse(cart);
    }

    public CartResponse addItem(String userId, CartItemRequest request) {
        validateUser(userId);

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));

        CartItem cartItem = new CartItem(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getBasePrice(),
                request.quantity(),
                request.platform() != null ? request.platform() : "Default"
        );

        cart.addItem(cartItem);
        cart = cartRepository.save(cart);

        log.info("Added product {} to cart for user {}", product.getId(), userId);
        return mapToCartResponse(cart);
    }

    public CartResponse updateItemQuantity(String userId, String productId, int quantity) {
        validateUser(userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.updateItemQuantity(productId, quantity);
        cart = cartRepository.save(cart);

        log.info("Updated quantity for product {} in cart for user {} to {}", productId, userId, quantity);
        return mapToCartResponse(cart);
    }

    public CartResponse removeItem(String userId, String productId) {
        validateUser(userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.removeItem(productId);
        cart = cartRepository.save(cart);

        log.info("Removed product {} from cart for user {}", productId, userId);
        return mapToCartResponse(cart);
    }

    public void clearCart(String userId) {
        validateUser(userId);

        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            cartOpt.get().clear();
            cartRepository.save(cartOpt.get());
            log.info("Cleared cart for user {}", userId);
        }
    }

    private void validateUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Cart createEmptyCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cart;
    }

    private CartResponse mapToCartResponse(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getItems(),
                cart.getTotalAmount(),
                cart.getTotalItems()
        );
    }
}