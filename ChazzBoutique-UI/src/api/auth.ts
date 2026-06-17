import { http } from "./http";

export type LoginResponse = {
  usuarioId: number;
  nombreUsuario: string;
  rol: string;
};

export function login(nombreUsuario: string, contrasena: string) {
  return http.post<LoginResponse>("/api/auth/login", { nombreUsuario, contrasena });
}
