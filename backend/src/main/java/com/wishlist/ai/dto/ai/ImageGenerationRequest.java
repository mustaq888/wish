package com.wishlist.ai.dto.ai;

import jakarta.validation.constraints.NotBlank;

public record ImageGenerationRequest(@NotBlank String prompt) {
}
