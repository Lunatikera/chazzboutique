import { useState, type FormEvent } from "react";
import "../styles/login.css";
import { useAuth } from "../auth/AuthContext";

export default function LoginPage() {
  const { login } = useAuth();

  const [nombreUsuario, setNombreUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const puedeEnviar = nombreUsuario.trim().length > 0 && contrasena.length > 0;

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    if (!puedeEnviar || loading) return;

    setError(null);
    setLoading(true);
    try {
      await login(nombreUsuario.trim(), contrasena);
    } catch (err) {
      setError((err as Error).message || "No se pudo iniciar sesión");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="login">
      <form className="login__card" onSubmit={onSubmit}>
        <img
          className="login__logo"
          src="/images/chazzLogoBlack.png"
          alt="Chazz Boutique"
          draggable={false}
        />

        <h1 className="login__title">Iniciar sesión</h1>

        <label className="login__field">
          <span className="login__label">Usuario</span>
          <input
            className="login__input"
            type="text"
            value={nombreUsuario}
            onChange={(e) => setNombreUsuario(e.target.value)}
            autoComplete="username"
            autoFocus
          />
        </label>

        <label className="login__field">
          <span className="login__label">Contraseña</span>
          <input
            className="login__input"
            type="password"
            value={contrasena}
            onChange={(e) => setContrasena(e.target.value)}
            autoComplete="current-password"
          />
        </label>

        {error && <p className="login__error">{error}</p>}

        <button className="login__submit" type="submit" disabled={!puedeEnviar || loading}>
          {loading ? "Entrando…" : "Entrar"}
        </button>
      </form>
    </div>
  );
}
