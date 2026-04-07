package com.wishlist.ai.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "reviews")
public class Review extends BaseEntity {

    private BigDecimal rating;

    private String comment;

    private String userId;

    private String productId;
}
