package com.wishlist.ai.controller;

import com.wishlist.ai.dto.cart.CartItemRequest;
import com.wishlist.ai.dto.cart.CartResponse;
import com.wishlist.ai.security.AppUserDetails;
import com.wishlist.ai.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal AppUserDetails user) {
        return cartService.getCart(user.getId());
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItem(@AuthenticationPrincipal AppUserDetails user,
                                @Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(user.getId(), request);
    }

    @PutMapping("/items/{productId}")
    public CartResponse updateItemQuantity(@AuthenticationPrincipal AppUserDetails user,
                                           @PathVariable String productId,
                                           @RequestParam @Valid @Min(0) int quantity) {
        return cartService.updateItemQuantity(user.getId(), productId, quantity);
    }

    @DeleteMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@AuthenticationPrincipal AppUserDetails user,
                           @PathVariable String productId) {
        cartService.removeItem(user.getId(), productId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@AuthenticationPrincipal AppUserDetails user) {
        cartService.clearCart(user.getId());
    }
}