package com.wishlist.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WishlistPriceComparisonApplication {

    public static void main(String[] args) {
        SpringApplication.run(WishlistPriceComparisonApplication.class, args);
    }
}
