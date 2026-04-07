import { FormEvent } from "react";
import { ProductFilters } from "../types";

type SearchFiltersProps = {
  filters: ProductFilters;
  categories: string[];
  resultCount: number;
  onChange: (patch: Partial<ProductFilters>) => void;
  onSubmit: () => void;
  onReset: () => void;
  onQuickCategory: (category: string) => void;
};

export function SearchFilters({ filters, categories, resultCount, onChange, onSubmit, onReset, onQuickCategory }: SearchFiltersProps) {
  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    onSubmit();
  }

  return (
    <section className="rounded-[2rem] bg-white p-6 shadow-soft">
      <div className="flex flex-col gap-3 border-b border-slate-100 pb-5 lg:flex-row lg:items-end lg:justify-between">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.24em] text-sky-700">Marketplace Search</p>
          <h2 className="mt-2 font-display text-3xl font-extrabold text-ink">Find products across every sector</h2>
          <p className="mt-2 text-sm text-slate-500">{resultCount} results ready for compare, wishlist, and cross-platform checking.</p>
        </div>
        <div className="flex flex-wrap gap-2">
          {categories.map((category) => (
            <button
              key={category}
              type="button"
              className={`rounded-full px-4 py-2 text-sm font-semibold transition ${
                filters.category === category ? "bg-sky-600 text-white" : "bg-slate-100 text-slate-700"
              }`}
              onClick={() => onQuickCategory(category)}
            >
              {category}
            </button>
          ))}
        </div>
      </div>

      <form className="mt-5 grid gap-4 lg:grid-cols-[2.2fr_1fr_1fr_1fr_auto_auto]" onSubmit={handleSubmit}>
        <input
          className="rounded-2xl border border-slate-200 px-4 py-3 text-sm"
          placeholder="Search for bulbs, computers, mixers, skincare, fashion, and more"
          value={filters.query}
          onChange={(event) => onChange({ query: event.target.value })}
        />
        <select className="rounded-2xl border border-slate-200 px-4 py-3 text-sm" value={filters.category} onChange={(event) => onChange({ category: event.target.value })}>
          <option value="">All sectors</option>
          {categories.map((category) => (
            <option key={category} value={category}>
              {category}
            </option>
          ))}
        </select>
        <select className="rounded-2xl border border-slate-200 px-4 py-3 text-sm" value={filters.minRating} onChange={(event) => onChange({ minRating: event.target.value })}>
          <option value="">Any rating</option>
          <option value="4.0">4.0+</option>
          <option value="4.3">4.3+</option>
          <option value="4.5">4.5+</option>
        </select>
        <select className="rounded-2xl border border-slate-200 px-4 py-3 text-sm" value={filters.sort} onChange={(event) => onChange({ sort: event.target.value as ProductFilters["sort"] })}>
          <option value="price-asc">Lowest price</option>
          <option value="price-desc">Highest price</option>
          <option value="rating-desc">Top rated</option>
          <option value="name-asc">Name A-Z</option>
        </select>
        <button className="rounded-2xl bg-coral px-5 py-3 text-sm font-semibold text-white" type="submit">
          Search
        </button>
        <button className="rounded-2xl border border-slate-200 px-5 py-3 text-sm font-semibold text-slate-700" type="button" onClick={onReset}>
          Clear
        </button>
      </form>
    </section>
  );
}
