# Wish List Products Price Comparison Website with AI Features

This folder contains a new Java full-stack implementation added beside the legacy Django codebase so the original app remains untouched.

Important update: the backend has been converted to use local MongoDB in VS Code instead of Docker/PostgreSQL. See `MONGODB_SETUP.md` in this folder for the current setup instructions.

## Stack

- Backend: Spring Boot 3.5.12, Java 21, Spring Data MongoDB, Spring Security, JWT, OpenAPI
- Frontend: React 19.2, Vite 8, Tailwind CSS, React Query, Axios
- Database: MongoDB local instance
- AI layer: Mock-ready image generation, recommendations, and price prediction
- Local workflow: MongoDB in VS Code or MongoDB Compass

## Folder Structure

```text
wishlist-price-comparison-java-ai/
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│       ├── main/java/com/wishlist/ai/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── domain/
│       │   ├── dto/
│       │   ├── exception/
│       │   ├── repository/
│       │   ├── security/
│       │   └── service/
│       ├── main/resources/
│       └── test/java/com/wishlist/ai/
├── frontend/
│   ├── src/components/
│   ├── package.json
│   ├── vite.config.ts
│   └── tailwind.config.js
└── docker-compose.yml
```

## Core Backend Modules

- `domain`: `User`, `Product`, `WishlistItem`, `PriceComparison`, `ProductImage`, `Review`
- `controller`: `/api/auth`, `/api/products`, `/api/wishlist`, `/api/compare`, `/api/recommendations`
- `security`: JWT token generation, auth filter, Spring Security stateless config
- `service`: product search, wishlist logic, price comparison, recommendation engine, AI image generation
- `exception`: global handler for clean API error responses

## Mongo Document Relationships

- `User` one-to-many `WishlistItem`
- `User` one-to-many `Review`
- `Product` one-to-many `PriceComparison`
- `Product` one-to-many `ProductImage`
- `Product` one-to-many `Review`
- `WishlistItem` acts as the join table between `User` and `Product`

## Database Collections

```text
users
products
wishlist_items
price_comparisons
product_images
reviews
```

## API Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/products`
- `GET /api/products/{productId}`
- `POST /api/products/{productId}/generate-image`
- `GET /api/wishlist`
- `POST /api/wishlist`
- `DELETE /api/wishlist`
- `GET /api/compare/{productId}`
- `GET /api/compare?productIds=1,2`
- `GET /api/recommendations`
- `GET /api/recommendations/trending`

Swagger UI:

- `http://localhost:8080/swagger-ui.html`

## AI Features

- AI image generation:
  `AiProductImageService` is structured as the integration point for OpenAI Images or Stable Diffusion. It currently returns a mock URL so the flow works immediately.
- Recommendation system:
  `RecommendationService` recommends based on wishlist categories and excludes already-saved products.
- Price prediction:
  `PricePredictionService` simulates a lightweight ML-style forecast using demand and rating signals.
- Smart search:
  `/api/products` supports keyword, category, price, rating, sorting, and pagination and is ready for future NLP/vector search.

## Setup

Backend:

```bash
cd wishlist-price-comparison-java-ai/backend
mvn spring-boot:run
```

Frontend:

```bash
cd wishlist-price-comparison-java-ai/frontend
npm install
npm run dev
```

MongoDB:

```bash
Start MongoDB locally, then run the backend and frontend from separate terminals.
```

## Environment Variables

```bash
MONGODB_URI=mongodb://localhost:27017/wishlist_ai
JWT_SECRET=replace-with-a-long-secret
ALLOWED_ORIGINS=http://localhost:5173
OPENAI_API_KEY=optional
```

## Production Notes

- Add Mongo migrations with Mongock or a similar migration tool if you want versioned schema changes
- Store secrets in a secret manager
- Replace the mock image provider with OpenAI or Stable Diffusion
- Add Redis if you want distributed caching
- Add integration tests for auth and wishlist workflows

## Version Notes

Version choices were aligned to official docs/pages current on April 7, 2026:

- Spring Boot 3.5.12: https://spring.io/blog/2026/03/19/spring-boot-3-5-12-available-now
- React 19.2: https://react.dev/versions
- Vite 8: https://vite.dev/blog/announcing-vite8
