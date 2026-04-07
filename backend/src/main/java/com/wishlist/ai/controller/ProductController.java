package com.wishlist.ai.controller;

import com.wishlist.ai.dto.ai.ImageGenerationRequest;
import com.wishlist.ai.dto.product.ProductResponse;
import com.wishlist.ai.service.AiProductImageService;
import com.wishlist.ai.service.ProductService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final AiProductImageService aiProductImageService;

    @GetMapping
    public Page<ProductResponse> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "basePrice") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return productService.search(query, category, minPrice, maxPrice, minRating, page, size, sortBy, sortDirection);
    }

    @GetMapping("/{productId}")
    public ProductResponse getById(@PathVariable String productId) {
        return productService.getById(productId);
    }

    @PostMapping("/{productId}/generate-image")
    public String generateImage(@PathVariable String productId, @Valid @RequestBody ImageGenerationRequest request) {
        return aiProductImageService.generate(productId, request);
    }
}
