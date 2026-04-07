import { FormEvent, useState } from "react";

type AuthPanelProps = {
  onLogin: (payload: { email: string; password: string }) => void;
  onRegister: (payload: { fullName: string; email: string; password: string }) => void;
  loading: boolean;
};

export function AuthPanel({ onLogin, onRegister, loading }: AuthPanelProps) {
  const [mode, setMode] = useState<"login" | "register">("login");
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (mode === "login") {
      onLogin({ email, password });
      return;
    }
    onRegister({ fullName, email, password });
  }

  return (
    <section className="rounded-[2rem] bg-white p-6 shadow-soft">
      <div className="mb-4 flex items-center justify-between">
        <div>
          <p className="text-sm uppercase tracking-[0.2em] text-slate-400">Authentication</p>
          <h2 className="font-display text-2xl font-bold text-ink">JWT access</h2>
        </div>
        <div className="rounded-full bg-slate-100 p-1 text-sm">
          <button type="button" className={`rounded-full px-4 py-2 ${mode === "login" ? "bg-ink text-white" : "text-slate-500"}`} onClick={() => setMode("login")}>
            Login
          </button>
          <button type="button" className={`rounded-full px-4 py-2 ${mode === "register" ? "bg-ink text-white" : "text-slate-500"}`} onClick={() => setMode("register")}>
            Register
          </button>
        </div>
      </div>
      <form className="space-y-3" onSubmit={handleSubmit}>
        {mode === "register" && (
          <input
            className="w-full rounded-2xl border border-slate-200 px-4 py-3"
            placeholder="Full name"
            value={fullName}
            onChange={(event) => setFullName(event.target.value)}
          />
        )}
        <input
          className="w-full rounded-2xl border border-slate-200 px-4 py-3"
          placeholder="Email"
          type="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
        />
        <input
          className="w-full rounded-2xl border border-slate-200 px-4 py-3"
          placeholder="Password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
        />
        <button className="w-full rounded-2xl bg-ink px-4 py-3 font-semibold text-white" disabled={loading}>
          {loading ? "Please wait..." : mode === "login" ? "Login" : "Create account"}
        </button>
      </form>
    </section>
  );
}
