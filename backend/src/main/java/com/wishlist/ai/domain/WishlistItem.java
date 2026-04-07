package com.wishlist.ai.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "wishlist_items")
@CompoundIndex(name = "user_product_unique_idx", def = "{'userId': 1, 'productId': 1}", unique = true)
public class WishlistItem extends BaseEntity {

    private String userId;

    private String productId;
}
