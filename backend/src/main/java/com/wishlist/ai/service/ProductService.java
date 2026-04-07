package com.wishlist.ai.service;

import com.wishlist.ai.domain.Product;
import com.wishlist.ai.dto.product.ProductResponse;
import com.wishlist.ai.exception.ResourceNotFoundException;
import com.wishlist.ai.repository.PriceComparisonRepository;
import com.wishlist.ai.repository.ProductImageRepository;
import com.wishlist.ai.repository.ProductRepository;
import com.wishlist.ai.repository.WishlistItemRepository;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PriceComparisonRepository priceComparisonRepository;
    private final ProductImageRepository productImageRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductMapper productMapper;
    private final PricePredictionService pricePredictionService;

    @Cacheable("products")
    public Page<ProductResponse> search(String query, String category, BigDecimal minPrice, BigDecimal maxPrice,
                                        BigDecimal minRating, int page, int size, String sortBy, String sortDirection) {
        var direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        log.info("Searching products query={} category={}", query, category);
        var filteredProducts = filterProducts(query, category, minPrice, maxPrice, minRating);
        var sortedProducts = sortProducts(filteredProducts, sortBy, direction);
        var start = Math.min((int) pageable.getOffset(), sortedProducts.size());
        var end = Math.min(start + pageable.getPageSize(), sortedProducts.size());
        var pagedProducts = sortedProducts.subList(start, end)
                .stream()
                .map(this::toResponse)
                .toList();
        return new PageImpl<>(pagedProducts, pageable, sortedProducts.size());
    }

    public ProductResponse getById(String productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return toResponse(product);
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private List<Product> filterProducts(String query, String category, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal minRating) {
        Stream<Product> stream = productRepository.findByActiveTrue().stream();
        if (query != null && !query.isBlank()) {
            var lowered = query.toLowerCase(Locale.ROOT);
            stream = stream.filter(product ->
                    product.getName().toLowerCase(Locale.ROOT).contains(lowered)
                            || product.getBrand().toLowerCase(Locale.ROOT).contains(lowered));
        }
        if (category != null && !category.isBlank()) {
            stream = stream.filter(product -> product.getCategory().equalsIgnoreCase(category));
        }
        if (minPrice != null) {
            stream = stream.filter(product -> product.getBasePrice().compareTo(minPrice) >= 0);
        }
        if (maxPrice != null) {
            stream = stream.filter(product -> product.getBasePrice().compareTo(maxPrice) <= 0);
        }
        if (minRating != null) {
            stream = stream.filter(product -> product.getAverageRating().compareTo(minRating) >= 0);
        }
        return stream.toList();
    }

    private List<Product> sortProducts(List<Product> products, String sortBy, Sort.Direction direction) {
        Comparator<Product> comparator = switch (sortBy) {
            case "name" -> Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
            case "averageRating" -> Comparator.comparing(Product::getAverageRating);
            default -> Comparator.comparing(Product::getBasePrice);
        };
        if (direction == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return products.stream().sorted(comparator).toList();
    }

    private ProductResponse toResponse(Product product) {
        return productMapper.toResponse(
                product,
                pricePredictionService.predict(product, wishlistItemRepository.countByProductId(product.getId())),
                priceComparisonRepository.findByProductIdOrderByListedPriceAsc(product.getId()),
                productImageRepository.findByProductIdOrderByCreatedAtDesc(product.getId())
        );
    }
}
