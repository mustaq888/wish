package com.wishlist.ai.controller;

import com.wishlist.ai.dto.wishlist.WishlistItemResponse;
import com.wishlist.ai.dto.wishlist.WishlistRequest;
import com.wishlist.ai.security.AppUserDetails;
import com.wishlist.ai.service.WishlistService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public List<WishlistItemResponse> getWishlist(@AuthenticationPrincipal AppUserDetails user) {
        return wishlistService.getWishlist(user.getId());
    }

    @PostMapping
    public List<WishlistItemResponse> add(@AuthenticationPrincipal AppUserDetails user,
                                          @Valid @RequestBody WishlistRequest request) {
        return wishlistService.addProduct(user.getId(), request.productId());
    }

    @DeleteMapping
    public void remove(@AuthenticationPrincipal AppUserDetails user,
                       @Valid @RequestBody WishlistRequest request) {
        wishlistService.removeProduct(user.getId(), request.productId());
    }
}
