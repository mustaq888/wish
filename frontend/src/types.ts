export type PlatformPrice = {
  platformName: string;
  listedPrice: number;
  shippingCost: number;
  totalPrice: number;
  inStock: boolean;
  productUrl: string;
};

export type Product = {
  id: string;
  name: string;
  description: string;
  category: string;
  brand: string;
  imageUrl: string;
  basePrice: number;
  averageRating: number;
  predictedNextPrice: number;
  comparisons: {
    id: string;
    platformName: string;
    productUrl: string;
    listedPrice: number;
    shippingCost: number;
    totalPrice: number;
    inStock: boolean;
    confidenceScore: number;
  }[];
  aiImages: string[];
};

export type ProductFilters = {
  query: string;
  category: string;
  minRating: string;
  sort: "price-asc" | "price-desc" | "rating-desc" | "name-asc";
};

export type ProductsPage = {
  content: Product[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
};

export type Recommendation = {
  productId: string;
  name: string;
  reason: string;
  imageUrl: string;
  price: number;
};

export type WishlistItem = {
  wishlistItemId: string;
  productId: string;
  name: string;
  brand: string;
  category: string;
  imageUrl: string;
  price: number;
  predictedNextPrice: number;
};

export type Comparison = {
  productId: string;
  productName: string;
  predictedNextPrice: number;
  platforms: PlatformPrice[];
};
