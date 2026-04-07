package com.wishlist.ai.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItem {

    private String productId;

    private String productName;

    private String productImageUrl;

    private BigDecimal price;

    private int quantity;

    private String platform; // The platform where this price was found

    public CartItem() {}

    public CartItem(String productId, String productName, String productImageUrl, BigDecimal price, int quantity, String platform) {
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.quantity = quantity;
        this.platform = platform;
    }
}