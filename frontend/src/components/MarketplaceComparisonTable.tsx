import { Comparison } from "../types";

type MarketplaceComparisonTableProps = {
  data?: Comparison;
};

export function MarketplaceComparisonTable({ data }: MarketplaceComparisonTableProps) {
  if (!data) {
    return (
      <section className="rounded-[2rem] bg-white p-6 shadow-soft">
        <p className="text-slate-500">Choose a product card and we’ll show every platform, landed price, and direct store links.</p>
      </section>
    );
  }

  return (
    <section className="rounded-[2rem] bg-white p-6 shadow-soft">
      <div className="mb-4 flex items-center justify-between">
        <div>
          <p className="text-sm uppercase tracking-[0.2em] text-slate-400">Comparison table</p>
          <h2 className="font-display text-2xl font-bold text-ink">{data.productName}</h2>
        </div>
        <p className="rounded-full bg-coral/10 px-4 py-2 text-sm font-semibold text-coral">
          AI next-price forecast: ${data.predictedNextPrice.toFixed(2)}
        </p>
      </div>
      <div className="overflow-x-auto">
        <table className="min-w-full text-left text-sm">
          <thead>
            <tr className="border-b border-slate-200 text-slate-400">
              <th className="pb-3">Platform</th>
              <th className="pb-3">Listed</th>
              <th className="pb-3">Shipping</th>
              <th className="pb-3">Total</th>
              <th className="pb-3">Stock</th>
              <th className="pb-3 text-right">Visit</th>
            </tr>
          </thead>
          <tbody>
            {data.platforms.map((platform) => (
              <tr key={platform.platformName} className="border-b border-slate-100">
                <td className="py-4 font-semibold text-ink">{platform.platformName}</td>
                <td className="py-4">${platform.listedPrice.toFixed(2)}</td>
                <td className="py-4">${platform.shippingCost.toFixed(2)}</td>
                <td className="py-4 font-bold text-coral">${platform.totalPrice.toFixed(2)}</td>
                <td className="py-4">{platform.inStock ? "In Stock" : "Out of Stock"}</td>
                <td className="py-4 text-right">
                  <a className="rounded-full bg-sky-100 px-4 py-2 text-sm font-semibold text-sky-700" href={platform.productUrl} target="_blank" rel="noreferrer">
                    Open
                  </a>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
