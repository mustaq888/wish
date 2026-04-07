package com.wishlist.ai.service;

import com.wishlist.ai.config.AppProperties;
import com.wishlist.ai.domain.ProductImage;
import com.wishlist.ai.dto.ai.ImageGenerationRequest;
import com.wishlist.ai.exception.ResourceNotFoundException;
import com.wishlist.ai.repository.ProductImageRepository;
import com.wishlist.ai.repository.ProductRepository;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final AppProperties appProperties;

    public String generate(String productId, ImageGenerationRequest request) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        var encodedPrompt = URLEncoder.encode(request.prompt(), StandardCharsets.UTF_8);
        var generatedUrl = "https://images.unsplash.com/photo-1512436991641-6745cdb1723?prompt=" + encodedPrompt;

        var image = new ProductImage();
        image.setProductId(product.getId());
        image.setPrompt(request.prompt());
        image.setProvider(appProperties.ai().imageProvider());
        image.setImageUrl(generatedUrl);
        productImageRepository.save(image);
        log.info("Generated AI image for product {}", productId);
        return generatedUrl;
    }
}
