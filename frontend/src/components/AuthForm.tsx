import { FormEvent, useState } from "react";

type AuthFormProps = {
  mode: "login" | "register";
  loading: boolean;
  onLogin: (payload: { email: string; password: string }) => void;
  onRegister: (payload: { fullName: string; email: string; password: string }) => void;
};

export function AuthForm({ mode, loading, onLogin, onRegister }: AuthFormProps) {
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
    <form className="space-y-4" onSubmit={handleSubmit}>
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
  );
}
