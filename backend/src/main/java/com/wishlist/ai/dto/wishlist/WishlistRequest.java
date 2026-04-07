package com.wishlist.ai.dto.wishlist;

import jakarta.validation.constraints.NotBlank;

public record WishlistRequest(@NotBlank String productId) {
}
