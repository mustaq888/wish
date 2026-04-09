type HeaderProps = {
  authenticated: boolean;
  userName?: string;
  onLogout: () => void;
  productCount: number;
  categoryCount: number;
};

export function Header({ authenticated, userName, onLogout, productCount, categoryCount }: HeaderProps) {
  return (
    <header className="border-b border-slate-200/70 bg-white/90 backdrop-blur">
      <div className="mx-auto flex w-full max-w-7xl flex-col gap-4 px-6 py-5 lg:flex-row lg:items-center lg:justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <span className="rounded-full bg-sky-100 px-3 py-1 text-xs font-semibold uppercase tracking-[0.2em] text-sky-700">Flipkart x Amazon vibe</span>
            <span className="rounded-full bg-amber-100 px-3 py-1 text-xs font-semibold uppercase tracking-[0.2em] text-amber-700">Live compare</span>
          </div>
          <p className="font-display text-3xl font-extrabold text-ink">WishList comparision product</p>
          <p className="text-sm text-slate-500">Search, compare, and shortlist products across {categoryCount} sectors and {productCount}+ listings.</p>
        </div>
        <div className="flex flex-wrap items-center gap-3">
          <span className="rounded-full bg-slate-100 px-4 py-2 text-sm font-semibold text-slate-700">{categoryCount} sectors</span>
          <span className="rounded-full bg-slate-100 px-4 py-2 text-sm font-semibold text-slate-700">{productCount}+ products</span>
          {authenticated ? (
            <>
              <span className="rounded-full bg-emerald-50 px-4 py-2 text-sm font-semibold text-emerald-700">
                {userName ? `Logged in: ${userName}` : "Logged in"}
              </span>
              <button
                className="rounded-full bg-ink px-5 py-3 text-sm font-semibold text-white transition hover:-translate-y-0.5"
                onClick={onLogout}
              >
                Logout
              </button>
            </>
          ) : (
            <span className="rounded-full bg-coral/10 px-5 py-3 text-sm font-semibold text-coral">Login</span>
          )}
        </div>
      </div>
    </header>
  );
}
