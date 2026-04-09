package com.wishlist.ai.dto.auth;

public record CurrentUserResponse(
        String userId,
        String fullName,
        String email
) {
}
