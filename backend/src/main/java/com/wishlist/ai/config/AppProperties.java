package com.wishlist.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Jwt jwt,
        Cors cors,
        Ai ai
) {
    public record Jwt(String secret, long expirationMinutes) {}
    public record Cors(String allowedOrigins) {}
    public record Ai(String imageProvider, String openaiApiKey) {}
}
