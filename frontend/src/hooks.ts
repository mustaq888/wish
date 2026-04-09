import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import api from "./api";
import { Comparison, CurrentUser, ProductFilters, ProductsPage, Recommendation, WishlistItem } from "./types";

function ensureProductsPage(value: unknown): ProductsPage {
  if (
    value &&
    typeof value === "object" &&
    "content" in value &&
    Array.isArray((value as ProductsPage).content) &&
    "totalElements" in value
  ) {
    return value as ProductsPage;
  }

  throw new Error("Invalid products response");
}

function ensureRecommendations(value: unknown): Recommendation[] {
  if (Array.isArray(value)) {
    return value as Recommendation[];
  }

  throw new Error("Invalid recommendations response");
}

function ensureComparison(value: unknown): Comparison {
  if (
    value &&
    typeof value === "object" &&
    "productId" in value &&
    "platforms" in value &&
    Array.isArray((value as Comparison).platforms)
  ) {
    return value as Comparison;
  }

  throw new Error("Invalid comparison response");
}

function ensureWishlist(value: unknown): WishlistItem[] {
  if (Array.isArray(value)) {
    return value as WishlistItem[];
  }

  throw new Error("Invalid wishlist response");
}

function ensureCurrentUser(value: unknown): CurrentUser {
  if (
    value &&
    typeof value === "object" &&
    "userId" in value &&
    "fullName" in value &&
    "email" in value
  ) {
    return value as CurrentUser;
  }

  throw new Error("Invalid current user response");
}

function toSortParams(sort: ProductFilters["sort"]) {
  switch (sort) {
    case "price-desc":
      return { sortBy: "basePrice", sortDirection: "desc" as const };
    case "rating-desc":
      return { sortBy: "averageRating", sortDirection: "desc" as const };
    case "name-asc":
      return { sortBy: "name", sortDirection: "asc" as const };
    default:
      return { sortBy: "basePrice", sortDirection: "asc" as const };
  }
}

export function useProducts(filters: ProductFilters) {
  const sortParams = toSortParams(filters.sort);

  return useQuery({
    queryKey: ["products", filters],
    queryFn: async () => {
      const response = await api.get("/api/products", {
        params: {
          page: 0,
          size: 24,
          query: filters.query || undefined,
          category: filters.category || undefined,
          minRating: filters.minRating || undefined,
          ...sortParams
        }
      });
      return ensureProductsPage(response.data);
    }
  });
}

export function useTrending() {
  return useQuery({
    queryKey: ["recommendations", "trending"],
    queryFn: async () => {
      const response = await api.get("/api/recommendations/trending");
      return ensureRecommendations(response.data);
    }
  });
}

export function useCompare(productId?: string) {
  return useQuery({
    queryKey: ["compare", productId],
    enabled: Boolean(productId),
    queryFn: async () => {
      const response = await api.get(`/api/compare/${productId}`);
      return ensureComparison(response.data);
    }
  });
}

export function useWishlist(enabled: boolean) {
  return useQuery({
    queryKey: ["wishlist"],
    enabled,
    queryFn: async () => {
      const response = await api.get("/api/wishlist");
      return ensureWishlist(response.data);
    }
  });
}

export function useCurrentUser(enabled: boolean) {
  return useQuery({
    queryKey: ["auth", "me"],
    enabled,
    retry: false,
    queryFn: async () => {
      const response = await api.get("/api/auth/me");
      return ensureCurrentUser(response.data);
    }
  });
}

export function useLogin() {
  return useMutation({
    mutationFn: async (payload: { email: string; password: string }) => {
      const response = await api.post("/api/auth/login", payload);
      return response.data as { token: string; userId: string; fullName: string; email: string };
    }
  });
}

export function useRegister() {
  return useMutation({
    mutationFn: async (payload: { fullName: string; email: string; password: string }) => {
      const response = await api.post("/api/auth/register", payload);
      return response.data as { token: string; userId: string; fullName: string; email: string };
    }
  });
}

export function useAddToWishlist() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (productId: string) => {
      const response = await api.post("/api/wishlist", { productId });
      return response.data as WishlistItem[];
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["wishlist"] });
    }
  });
}
