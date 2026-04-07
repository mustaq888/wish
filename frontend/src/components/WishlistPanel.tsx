import { WishlistItem } from "../types";

type WishlistPanelProps = {
  items: WishlistItem[];
};

export function WishlistPanel({ items }: WishlistPanelProps) {
  return (
    <section className="rounded-[2rem] bg-white p-6 shadow-soft">
      <div className="mb-4 flex items-center justify-between">
        <div>
          <p className="text-sm uppercase tracking-[0.2em] text-slate-400">Wishlist</p>
          <h2 className="font-display text-2xl font-bold text-ink">Saved for later</h2>
        </div>
        <span className="rounded-full bg-slate-100 px-4 py-2 text-sm font-semibold text-slate-700">{items.length} items</span>
      </div>
      <div className="space-y-3">
        {items.length === 0 ? (
          <p className="text-slate-500">Log in to load your wishlist from the JWT-protected API.</p>
        ) : (
          items.map((item) => (
            <div key={item.wishlistItemId} className="flex items-center gap-4 rounded-2xl border border-slate-100 p-3">
              <img className="h-16 w-16 rounded-2xl object-cover" src={item.imageUrl} alt={item.name} />
              <div className="flex-1">
                <p className="font-semibold text-ink">{item.name}</p>
                <p className="text-sm text-slate-500">{item.brand} • {item.category}</p>
              </div>
              <div className="text-right">
                <p className="font-bold text-ink">${item.price.toFixed(2)}</p>
                <p className="text-xs text-coral">Predicted ${item.predictedNextPrice.toFixed(2)}</p>
              </div>
            </div>
          ))
        )}
      </div>
    </section>
  );
}
