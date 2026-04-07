package com.wishlist.ai.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "products")
public class Product extends BaseEntity {

    private String name;

    private String description;

    private String category;

    private String brand;

    private String imageUrl;

    private BigDecimal basePrice;

    private BigDecimal averageRating = BigDecimal.ZERO;

    private boolean active = true;
}
