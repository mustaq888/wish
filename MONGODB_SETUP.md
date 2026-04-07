# MongoDB Setup

The current backend is configured for local MongoDB, not Docker.

## Prerequisites

- Java 21
- Maven
- MongoDB Community Server running locally
- Optional: MongoDB VS Code extension or MongoDB Compass

## Connection

Default backend connection:

```bash
MONGODB_URI=mongodb://localhost:27017/wishlist_ai
```

## Run Backend

```bash
cd wishlist-price-comparison-java-ai/backend
mvn spring-boot:run
```

## Run Frontend

```bash
cd wishlist-price-comparison-java-ai/frontend
npm install
npm run dev
```

## Collections

When the backend starts for the first time, `DataSeeder` creates sample data in these collections:

- `products`
- `price_comparisons`
- `users`
- `wishlist_items`
- `product_images`
- `reviews`

## VS Code Workflow

1. Install the MongoDB extension in VS Code.
2. Connect to `mongodb://localhost:27017`.
3. Open the `wishlist_ai` database.
4. Start the Spring Boot backend.
5. Refresh the explorer to view the seeded collections.

## Notes

- Mongo relationships are modeled with reference IDs such as `productId` and `userId`.
- The frontend already expects string-based Mongo IDs from the API.
- Docker is no longer required for local development of this scaffold.
