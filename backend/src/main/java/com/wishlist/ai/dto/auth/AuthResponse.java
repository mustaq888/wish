package com.wishlist.ai.dto.auth;

public record AuthResponse(
        String token,
        String tokenType,
        String userId,
        String fullName,
        String email
) {
}
