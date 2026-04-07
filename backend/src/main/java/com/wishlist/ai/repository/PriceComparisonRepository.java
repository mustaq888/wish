package com.wishlist.ai.repository;

import com.wishlist.ai.domain.PriceComparison;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceComparisonRepository extends MongoRepository<PriceComparison, String> {
    List<PriceComparison> findByProductIdOrderByListedPriceAsc(String productId);
}
