import { Recommendation } from "../types";

type RecommendationsPanelProps = {
  items: Recommendation[];
};

export function RecommendationsPanel({ items }: RecommendationsPanelProps) {
  return (
    <section className="rounded-[2rem] bg-ink p-6 text-white shadow-soft">
      <p className="text-sm uppercase tracking-[0.2em] text-slate-300">AI recommendations</p>
      <h2 className="mt-2 font-display text-2xl font-bold">Behavior-aware picks</h2>
      <div className="mt-6 grid gap-4 md:grid-cols-3">
        {items.map((item) => (
          <article key={item.productId} className="rounded-[1.5rem] bg-white/10 p-4 backdrop-blur">
            <img className="mb-4 h-36 w-full rounded-[1.25rem] object-cover" src={item.imageUrl} alt={item.name} />
            <h3 className="font-semibold">{item.name}</h3>
            <p className="mt-2 text-sm text-slate-300">{item.reason}</p>
            <p className="mt-4 font-bold text-coral">${item.price.toFixed(2)}</p>
          </article>
        ))}
      </div>
    </section>
  );
}
