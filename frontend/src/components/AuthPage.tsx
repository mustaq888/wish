import { AuthForm } from "./AuthForm";

type AuthPageProps = {
  mode: "login" | "register";
  loading: boolean;
  onLogin: (payload: { email: string; password: string }) => void;
  onRegister: (payload: { fullName: string; email: string; password: string }) => void;
  onNavigate: (path: string) => void;
};

export function AuthPage({ mode, loading, onLogin, onRegister, onNavigate }: AuthPageProps) {
  const isLogin = mode === "login";

  return (
    <main className="mx-auto grid min-h-[calc(100vh-120px)] max-w-7xl gap-8 px-6 py-10 lg:grid-cols-[1.1fr_0.9fr]">
      <section className="rounded-[2.6rem] bg-gradient-to-br from-sky-700 via-sky-600 to-cyan-500 p-8 text-white shadow-soft">
        <p className="w-fit rounded-full bg-white/15 px-4 py-2 text-sm font-semibold uppercase tracking-[0.2em] text-sky-50">
          Account access
        </p>
        <h1 className="mt-5 max-w-2xl font-display text-5xl font-extrabold leading-tight">
          {isLogin ? "Welcome back to your product wishlist." : "Create your account and save products across stores."}
        </h1>
        <p className="mt-4 max-w-2xl text-lg text-sky-50/90">
          Compare deals, keep your shortlist in one place, and jump back into your saved picks whenever you want.
        </p>
      </section>

      <section className="self-center rounded-[2rem] bg-white p-6 shadow-soft">
        <div className="mb-6">
          <p className="text-sm uppercase tracking-[0.2em] text-slate-400">Authentication</p>
          <h2 className="font-display text-3xl font-bold text-ink">{isLogin ? "Login" : "Register"}</h2>
          <p className="mt-2 text-sm text-slate-500">
            {isLogin ? "Use your existing account to open your wishlist." : "Register once, then you can save products and track deals."}
          </p>
        </div>

        <AuthForm mode={mode} loading={loading} onLogin={onLogin} onRegister={onRegister} />

        <div className="mt-5 text-sm text-slate-500">
          {isLogin ? "Need an account?" : "Already have an account?"}{" "}
          <button className="font-semibold text-sky-700" onClick={() => onNavigate(isLogin ? "/register" : "/login")} type="button">
            {isLogin ? "Register here" : "Login here"}
          </button>
        </div>
      </section>
    </main>
  );
}
