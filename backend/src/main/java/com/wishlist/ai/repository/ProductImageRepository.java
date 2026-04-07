package com.wishlist.ai.repository;

import com.wishlist.ai.domain.ProductImage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductImageRepository extends MongoRepository<ProductImage, String> {
    List<ProductImage> findByProductIdOrderByCreatedAtDesc(String productId);
}
