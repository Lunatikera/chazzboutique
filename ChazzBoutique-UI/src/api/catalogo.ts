import { http } from "./http";

export type Categoria = {
  id: number;
  nombre: string;
  imagenUrl?: string | null;
};

/**
 * Lo que usa tu HomePage para renderizar la tabla.
 * (nota: algunos campos pueden venir vacíos si tu backend aún no los manda)
 */
export type VarianteCatalogo = {
  varianteId: number;
  codigoBarras: string; // por ahora el backend de /api/variantes no lo manda
  nombreProducto: string;

  categoriaNombre?: string | null; // por ahora el backend de /api/variantes no lo manda
  imagenUrl?: string | null;

  talla?: string | null;
  colorHex?: string | null;

  precioVenta: number;
  stock: number;
};

export type PagedResponse<T> = {
  page: number;
  pageSize: number;
  total: number;
  hasNext: boolean;
  items: T[];
};

type VarianteCardResponse = {
  id: number;
  nombreProducto: string;
  talla?: string | null;
  colorHex?: string | null;
  precioVenta: number;
  stock: number;
  imagenUrl?: string | null;
};

export function listarCategorias() {
  return http.get<Categoria[]>("/api/categorias");
}

/**
 * Backend actual:
 * GET /api/variantes?search=&page=&pageSize=
 */
export function buscarVariantes(params: {
  filtro?: string;
  pagina: number;
  tamanoPagina: number;
  categoriaId?: number | null;
}) {
  return http.get<PagedResponse<VarianteCardResponse>>("/api/variantes", {
    search: params.filtro ?? "",
    page: params.pagina,
    pageSize: params.tamanoPagina,
    categoriaId: params.categoriaId ?? undefined,
  });
}


/**
 * Adaptador: convierte lo que regresa /api/variantes a lo que tu tabla espera.
 * codigoBarras y categoriaNombre quedan vacíos por ahora (hasta que el backend los incluya).
 */
export function mapVarianteCardToCatalogo(v: VarianteCardResponse): VarianteCatalogo {
  return {
    varianteId: v.id,
    codigoBarras: "", // si lo quieres en tabla, lo agregamos al backend después
    nombreProducto: v.nombreProducto,
    categoriaNombre: null,
    imagenUrl: v.imagenUrl ?? null,
    talla: v.talla ?? null,
    colorHex: v.colorHex ?? null,
    precioVenta: Number(v.precioVenta ?? 0),
    stock: Number(v.stock ?? 0),
  };
}
