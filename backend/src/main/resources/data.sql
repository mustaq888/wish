INSERT INTO products (id, name, description, category, brand, image_url, base_price, average_rating, active, created_at, updated_at)
SELECT 1, 'Nike Air Zoom Pulse', 'Comfortable lifestyle sneakers with premium cushioning.', 'Footwear', 'Nike', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff', 119.99, 4.6, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = 1);

INSERT INTO products (id, name, description, category, brand, image_url, base_price, average_rating, active, created_at, updated_at)
SELECT 2, 'Apple Watch Series Pro', 'Premium smartwatch with fitness tracking and productivity tools.', 'Wearables', 'Apple', 'https://images.unsplash.com/photo-1546868871-7041f2a55e12', 399.99, 4.8, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = 2);

INSERT INTO price_comparisons (id, platform_name, product_url, listed_price, shipping_cost, in_stock, scraped_at, confidence_score, product_id, created_at, updated_at)
SELECT 1, 'Amazon', 'https://amazon.example/product/1', 119.99, 0.00, true, NOW(), 0.95, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM price_comparisons WHERE id = 1);

INSERT INTO price_comparisons (id, platform_name, product_url, listed_price, shipping_cost, in_stock, scraped_at, confidence_score, product_id, created_at, updated_at)
SELECT 2, 'Flipkart', 'https://flipkart.example/product/1', 114.49, 2.99, true, NOW(), 0.91, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM price_comparisons WHERE id = 2);

INSERT INTO price_comparisons (id, platform_name, product_url, listed_price, shipping_cost, in_stock, scraped_at, confidence_score, product_id, created_at, updated_at)
SELECT 3, 'BestBuy', 'https://bestbuy.example/product/2', 389.99, 0.00, true, NOW(), 0.93, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM price_comparisons WHERE id = 3);
