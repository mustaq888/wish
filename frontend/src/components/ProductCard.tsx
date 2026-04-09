import { Product } from "../types";

type ProductCardProps = {
  product: Product;
  onCompare: (productId: string) => void;
  onAddToWishlist: (productId: string) => void;
  disableWishlist: boolean;
};

export function ProductCard({ product, onCompare, onAddToWishlist, disableWishlist }: ProductCardProps) {
  const cheapest = product.comparisons[0];

  return (
    <article className="overflow-hidden rounded-[2rem] bg-white shadow-soft">
      <img className="h-56 w-full object-cover" src={product.imageUrl} alt={product.name} />
      <div className="space-y-4 p-5">
        <div className="flex items-start justify-between gap-4">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.2em] text-coral">{product.category}</p>
            <h3 className="font-display text-xl font-bold text-ink">{product.name}</h3>
            <p className="text-sm text-slate-500">{product.brand}</p>
          </div>
          <span className="rounded-full bg-amber-50 px-3 py-1 text-sm font-semibold text-amber-700">
            {product.averageRating}★
          </span>
        </div>
        <p className="line-clamp-3 text-sm text-slate-600">{product.description}</p>
        <div className="rounded-2xl bg-slate-50 p-4">
          <p className="text-xs uppercase tracking-[0.18em] text-slate-400">Best live price</p>
          <div className="mt-2 flex items-center justify-between">
            <span className="text-2xl font-bold text-ink">${cheapest?.totalPrice?.toFixed(2) ?? product.basePrice.toFixed(2)}</span>
            <span className="text-sm text-slate-500">{cheapest?.platformName ?? "Direct"}</span>
          </div>
          <p className="mt-2 text-sm text-slate-500">AI forecast: ${product.predictedNextPrice.toFixed(2)}</p>
        </div>
        <div className="flex gap-3">
          <button className="flex-1 rounded-2xl bg-ink px-4 py-3 font-semibold text-white" onClick={() => onCompare(product.id)}>
            Compare
          </button>
          <button
            className="flex-1 rounded-2xl border border-slate-200 px-4 py-3 font-semibold text-ink disabled:cursor-not-allowed disabled:opacity-50"
            onClick={() => onAddToWishlist(product.id)}
            disabled={disableWishlist}
          >
            Add to Wishlist
          </button>
        </div>
      </div>
    </article>
  );
}
