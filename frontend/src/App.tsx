import { useEffect, useState } from "react";
import { Header } from "./components/Header";
import { SearchFilters } from "./components/SearchFilters";
import { MarketplaceProductCard } from "./components/MarketplaceProductCard";
import { MarketplaceComparisonTable } from "./components/MarketplaceComparisonTable";
import { MarketplaceWishlistPanel } from "./components/MarketplaceWishlistPanel";
import { MarketplaceRecommendationsPanel } from "./components/MarketplaceRecommendationsPanel";
import { AuthPanel } from "./components/AuthPanel";
import { useAddToWishlist, useCompare, useCurrentUser, useLogin, useProducts, useRegister, useTrending, useWishlist } from "./hooks";
import { formatInr } from "./currency";
import { CurrentUser, ProductFilters } from "./types";

const categories = ["Computers", "Mobiles", "Audio", "Television", "Household", "Kitchen", "Appliances", "Furniture", "Fashion", "Beauty"];

const defaultFilters: ProductFilters = {
  query: "",
  category: "",
  minRating: "",
  sort: "price-asc"
};

export default function App() {
  const [token, setToken] = useState(() => localStorage.getItem("wishlist-token"));
  const [selectedProductId, setSelectedProductId] = useState<string | undefined>();
  const [draftFilters, setDraftFilters] = useState<ProductFilters>(defaultFilters);
  const [appliedFilters, setAppliedFilters] = useState<ProductFilters>(defaultFilters);
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null);
  const authenticated = Boolean(token);
  const productsQuery = useProducts(appliedFilters);
  const compareQuery = useCompare(selectedProductId);
  const trendingQuery = useTrending();
  const currentUserQuery = useCurrentUser(authenticated);
  const wishlistQuery = useWishlist(authenticated);
  const loginMutation = useLogin();
  const registerMutation = useRegister();
  const addToWishlistMutation = useAddToWishlist();

  useEffect(() => {
    if (token) {
      localStorage.setItem("wishlist-token", token);
      return;
    }
    setCurrentUser(null);
    localStorage.removeItem("wishlist-token");
  }, [token]);

  useEffect(() => {
    if (currentUserQuery.data) {
      setCurrentUser(currentUserQuery.data);
    }
  }, [currentUserQuery.data]);

  useEffect(() => {
    if (currentUserQuery.isError) {
      localStorage.removeItem("wishlist-token");
      setToken(null);
    }
  }, [currentUserQuery.isError]);

  function handleAuthSuccess(auth: { token: string; userId: string; fullName: string; email: string }) {
    setToken(auth.token);
    setCurrentUser({
      userId: auth.userId,
      fullName: auth.fullName,
      email: auth.email
    });
  }

  const products = productsQuery.data?.content ?? [];
  const totalProducts = productsQuery.data?.totalElements ?? 0;
  const heroProduct = products[0];
  const bestDeal = heroProduct?.comparisons[0];

  return (
    <div className="min-h-screen bg-[linear-gradient(180deg,_#eef4ff_0%,_#fff7e8_28%,_#f8fafc_58%,_#fffdfa_100%)] pb-16">
      <Header authenticated={authenticated} userName={currentUser?.fullName} onLogout={() => setToken(null)} productCount={totalProducts} categoryCount={categories.length} />

      <main className="mx-auto flex max-w-7xl flex-col gap-8 px-6 py-8">
        <section className="grid gap-6 lg:grid-cols-[1.45fr_0.9fr]">
          <div className="overflow-hidden rounded-[2.6rem] bg-gradient-to-br from-sky-700 via-sky-600 to-cyan-500 p-8 text-white shadow-soft">
            <p className="w-fit rounded-full bg-white/15 px-4 py-2 text-sm font-semibold uppercase tracking-[0.2em] text-sky-50">
              Smart compare across many stores
            </p>
            <h1 className="mt-5 max-w-3xl font-display text-5xl font-extrabold leading-tight">
              One storefront for electronics, household items, fashion, beauty, and everyday essentials.
            </h1>
            <p className="mt-4 max-w-2xl text-lg text-sky-50/90">
              Search like a marketplace, compare like a price engine, and shortlist products with AI-powered forecasts and multi-platform pricing.
            </p>
            <div className="mt-8 flex flex-wrap gap-3">
              <button className="rounded-full bg-white px-5 py-3 text-sm font-semibold text-sky-700" onClick={() => setAppliedFilters(draftFilters)}>
                Browse Deals
              </button>
              <button
                className="rounded-full border border-white/40 px-5 py-3 text-sm font-semibold text-white"
                onClick={() => {
                  setDraftFilters({ ...defaultFilters, category: "Household" });
                  setAppliedFilters({ ...defaultFilters, category: "Household" });
                }}
              >
                Explore Household
              </button>
            </div>
          </div>
          <div className="grid gap-4 sm:grid-cols-2">
            <div className="rounded-[2rem] bg-white p-5 shadow-soft">
              <p className="text-sm text-slate-500">Catalog coverage</p>
              <p className="mt-3 text-4xl font-bold text-ink">{totalProducts}+</p>
              <p className="mt-2 text-sm text-slate-500">Products across household, computers, kitchen, beauty, fashion, and more.</p>
            </div>
            <div className="rounded-[2rem] bg-amber-400 p-5 text-ink shadow-soft">
              <p className="text-sm font-semibold text-amber-950/70">Platforms tracked</p>
              <p className="mt-3 text-4xl font-bold">6/store</p>
              <p className="mt-2 text-sm text-amber-950/80">Amazon, Flipkart, Croma, Nykaa, Ikea, Pepperfry, and more depending on category.</p>
            </div>
            <div className="rounded-[2rem] bg-ink p-5 text-white shadow-soft">
              <p className="text-sm text-slate-300">Featured deal</p>
              <p className="mt-3 text-2xl font-bold">{heroProduct?.name ?? "Loading..."}</p>
              <p className="mt-2 text-sm text-slate-300">{bestDeal ? `${bestDeal.platformName} from ${formatInr(bestDeal.totalPrice)}` : "Comparisons loading"}</p>
            </div>
            <div className="rounded-[2rem] bg-white p-5 shadow-soft">
              <p className="text-sm text-slate-500">Search ready</p>
              <p className="mt-3 text-4xl font-bold text-ink">Live</p>
              <p className="mt-2 text-sm text-slate-500">Search, sector filtering, rating filters, compare buttons, and direct store links are active.</p>
            </div>
          </div>
        </section>

        {!authenticated && (
          <AuthPanel
            loading={loginMutation.isPending || registerMutation.isPending}
            onLogin={(payload) => loginMutation.mutate(payload, { onSuccess: (data) => handleAuthSuccess(data) })}
            onRegister={(payload) => registerMutation.mutate(payload, { onSuccess: (data) => handleAuthSuccess(data) })}
          />
        )}

        <SearchFilters
          filters={draftFilters}
          categories={categories}
          resultCount={totalProducts}
          onChange={(patch) => setDraftFilters((current) => ({ ...current, ...patch }))}
          onSubmit={() => setAppliedFilters(draftFilters)}
          onReset={() => {
            setDraftFilters(defaultFilters);
            setAppliedFilters(defaultFilters);
          }}
          onQuickCategory={(category) => {
            const next = { ...draftFilters, category };
            setDraftFilters(next);
            setAppliedFilters(next);
          }}
        />

        {productsQuery.isLoading ? (
          <section className="rounded-[2rem] bg-white p-6 text-slate-500 shadow-soft">Loading products...</section>
        ) : productsQuery.isError ? (
          <section className="rounded-[2rem] bg-white p-6 text-red-600 shadow-soft">Unable to load products.</section>
        ) : (
          <section className="space-y-5">
            <div className="flex flex-col gap-3 rounded-[2rem] bg-white p-5 shadow-soft lg:flex-row lg:items-center lg:justify-between">
              <div>
                <p className="text-sm uppercase tracking-[0.2em] text-slate-400">Results</p>
                <h2 className="font-display text-3xl font-bold text-ink">
                  {appliedFilters.category || "All sectors"}
                  {appliedFilters.query ? ` for "${appliedFilters.query}"` : ""}
                </h2>
              </div>
              <p className="text-sm text-slate-500">
                {totalProducts} products found. Use Compare to inspect every store and Best Deal to jump straight to the cheapest listing.
              </p>
            </div>

            <section className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
            {products.map((product) => (
              <MarketplaceProductCard
                key={product.id}
                product={product}
                onCompare={setSelectedProductId}
                onAddToWishlist={(productId) => addToWishlistMutation.mutate(productId)}
                disableWishlist={!authenticated || addToWishlistMutation.isPending}
              />
            ))}
            </section>
          </section>
        )}

        <section className="grid gap-6 xl:grid-cols-[1.4fr_0.6fr]">
          <MarketplaceComparisonTable data={compareQuery.data} />
          <MarketplaceWishlistPanel items={wishlistQuery.data ?? []} />
        </section>

        {trendingQuery.isLoading ? (
          <section className="rounded-[2rem] bg-ink p-6 text-white shadow-soft">Loading recommendations...</section>
        ) : (
          <MarketplaceRecommendationsPanel items={trendingQuery.data ?? []} />
        )}
      </main>
    </div>
  );
}
