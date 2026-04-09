import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import api from "./api";
import { Comparison, CurrentUser, ProductFilters, ProductsPage, Recommendation, WishlistItem } from "./types";

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
      return response.data as ProductsPage;
    }
  });
}

export function useTrending() {
  return useQuery({
    queryKey: ["recommendations", "trending"],
    queryFn: async () => {
      const response = await api.get("/api/recommendations/trending");
      return response.data as Recommendation[];
    }
  });
}

export function useCompare(productId?: string) {
  return useQuery({
    queryKey: ["compare", productId],
    enabled: Boolean(productId),
    queryFn: async () => {
      const response = await api.get(`/api/compare/${productId}`);
      return response.data as Comparison;
    }
  });
}

export function useWishlist(enabled: boolean) {
  return useQuery({
    queryKey: ["wishlist"],
    enabled,
    queryFn: async () => {
      const response = await api.get("/api/wishlist");
      return response.data as WishlistItem[];
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
      return response.data as CurrentUser;
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
