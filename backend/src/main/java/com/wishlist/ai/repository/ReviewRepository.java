package com.wishlist.ai.repository;

import com.wishlist.ai.domain.Review;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findTop5ByProductIdOrderByCreatedAtDesc(String productId);
}
