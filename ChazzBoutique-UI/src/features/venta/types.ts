import type { ToastState } from "../../components/ui/MotionToast";

export type VentaRow = {
  id: string;
  codigoBarras: string;
  nombre: string;
  cantidad: number;
  precio: number;
  colorHex?: string;
};

export type VentaToast = ToastState<"error" | "ok">;

export type VentaOk = {
  id: number;
  total: number;
  cambio: number;
};
