package com.wishlist.ai.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "product_images")
public class ProductImage extends BaseEntity {

    private String prompt;

    private String imageUrl;

    private String provider;

    private String productId;
}
