import type { ToastState } from "../../components/ui/MotionToast";
import type { VarianteCatalogo } from "../../api/catalogo";

export type CatalogoToast = ToastState<"ok" | "error">;

export type FeedState = {
  key: string;
  items: VarianteCatalogo[];
  total: number;
  hasMore: boolean;
  page: number;
};
