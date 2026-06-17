import { createContext, useContext, useState, type ReactNode } from "react";
import * as authApi from "../api/auth";

export type AuthUser = {
  usuarioId: number;
  nombreUsuario: string;
  rol: string;
};

type AuthContextValue = {
  user: AuthUser | null;
  login: (nombreUsuario: string, contrasena: string) => Promise<void>;
  logout: () => void;
};

const STORAGE_KEY = "chazz.auth";

const AuthContext = createContext<AuthContextValue | null>(null);

function readStoredUser(): AuthUser | null {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    const parsed = JSON.parse(raw);
    if (
      typeof parsed?.usuarioId === "number" &&
      typeof parsed?.nombreUsuario === "string" &&
      typeof parsed?.rol === "string"
    ) {
      return parsed as AuthUser;
    }
    return null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(readStoredUser);

  async function login(nombreUsuario: string, contrasena: string) {
    const res = await authApi.login(nombreUsuario, contrasena);
    const next: AuthUser = {
      usuarioId: res.usuarioId,
      nombreUsuario: res.nombreUsuario,
      rol: res.rol,
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(next));
    setUser(next);
  }

  function logout() {
    localStorage.removeItem(STORAGE_KEY);
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth debe usarse dentro de AuthProvider");
  return ctx;
}
