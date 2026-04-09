import { formatInr } from "../currency";
import { Product } from "../types";

type MarketplaceProductCardProps = {
  product: Product;
  onCompare: (productId: string) => void;
  onAddToWishlist: (productId: string) => void;
  disableWishlist: boolean;
};

export function MarketplaceProductCard({ product, onCompare, onAddToWishlist, disableWishlist }: MarketplaceProductCardProps) {
  const cheapest = product.comparisons[0];

  return (
    <article className="overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-soft transition hover:-translate-y-1">
      <div className="relative">
        <img className="h-56 w-full object-cover" src={product.imageUrl} alt={product.name} />
        <div className="absolute left-4 top-4 rounded-full bg-white/95 px-3 py-1 text-xs font-semibold uppercase tracking-[0.2em] text-sky-700 shadow">
          {product.category}
        </div>
      </div>
      <div className="space-y-4 p-5">
        <div className="flex items-start justify-between gap-4">
          <div className="min-w-0">
            <h3 className="font-display text-xl font-bold text-ink">{product.name}</h3>
            <p className="mt-1 text-sm text-slate-500">{product.brand}</p>
          </div>
          <span className="rounded-full bg-amber-50 px-3 py-1 text-sm font-semibold text-amber-700">{product.averageRating.toFixed(1)}★</span>
        </div>
        <p className="line-clamp-3 min-h-[60px] text-sm text-slate-600">{product.description}</p>
        <div className="grid gap-3 rounded-[1.5rem] bg-slate-50 p-4">
          <div className="flex items-center justify-between">
            <p className="text-xs uppercase tracking-[0.18em] text-slate-400">Best live price</p>
            <span className="rounded-full bg-emerald-100 px-3 py-1 text-xs font-semibold text-emerald-700">{product.comparisons.length} stores</span>
          </div>
          <div className="flex items-center justify-between gap-4">
            <span className="text-2xl font-bold text-ink">{formatInr(cheapest?.totalPrice ?? product.basePrice)}</span>
            <span className="text-right text-sm text-slate-500">{cheapest?.platformName ?? "Direct"}</span>
          </div>
          <p className="text-sm text-slate-500">AI forecast next move: {formatInr(product.predictedNextPrice)}</p>
        </div>
        <div className="flex flex-wrap gap-2 text-xs font-medium text-slate-500">
          {product.comparisons.slice(0, 4).map((comparison) => (
            <span key={comparison.id} className="rounded-full bg-slate-100 px-3 py-1">
              {comparison.platformName}
            </span>
          ))}
        </div>
        <div className="grid gap-3 sm:grid-cols-3">
          <button className="rounded-2xl bg-ink px-4 py-3 font-semibold text-white" onClick={() => onCompare(product.id)}>
            Compare
          </button>
          <button
            className="rounded-2xl border border-slate-200 px-4 py-3 font-semibold text-ink disabled:cursor-not-allowed disabled:opacity-50"
            onClick={() => onAddToWishlist(product.id)}
            disabled={disableWishlist}
          >
            Add to Wishlist
          </button>
          <a className="flex items-center justify-center rounded-2xl bg-sky-600 px-4 py-3 font-semibold text-white" href={cheapest?.productUrl ?? "#"} target="_blank" rel="noreferrer">
            Best Deal
          </a>
        </div>
      </div>
    </article>
  );
}
