import type { VarianteCatalogo } from "../../api/catalogo";

export type NormalizedVariantes = {
  list: VarianteCatalogo[];
  total: number;
  hasNext: boolean;
};

type BuscarVariantesParams = {
  filtro?: string;
  pagina: number;
  tamanoPagina: number;
  categoriaId?: number | null;
};

function asRecord(value: unknown): Record<string, unknown> | null {
  if (!value || typeof value !== "object") return null;
  return value as Record<string, unknown>;
}

function toNullableString(value: unknown) {
  if (value === null || value === undefined) return null;
  return String(value);
}

export function money(n: number) {
  return n.toLocaleString("es-MX", { style: "currency", currency: "MXN" });
}

export function apiImgSrc(path?: string | null) {
  if (!path) return "";
  if (/^https?:\/\//i.test(path)) return path;
  const base = (import.meta.env.VITE_API_BASE_URL || "").replace(/\/+$/g, "");
  const clean = ("/" + path).replace(/\/{2,}/g, "/");
  return base ? `${base}${clean}` : clean;
}

export function makeKey(catId: number | null, filtro: string) {
  const catPart = catId === null ? "all" : String(catId);
  const f = encodeURIComponent((filtro || "").trim());
  return `${catPart}|${f}`;
}

export function parseKey(key: string) {
  const i = key.indexOf("|");
  const catPart = i >= 0 ? key.slice(0, i) : key;
  const fEnc = i >= 0 ? key.slice(i + 1) : "";
  const parsedCatId = catPart === "all" ? null : Number(catPart);
  const catId = typeof parsedCatId === "number" && Number.isFinite(parsedCatId) ? parsedCatId : null;
  const filtro = decodeURIComponent(fEnc || "");
  return { catId, filtro };
}

export function clamp(n: number, min: number, max: number) {
  return Math.min(max, Math.max(min, n));
}

function normalizeVariante(value: unknown): VarianteCatalogo {
  const obj = asRecord(value);

  return {
    varianteId: Number(obj?.varianteId ?? obj?.id ?? 0),
    codigoBarras: String(obj?.codigoBarras ?? ""),
    nombreProducto: String(obj?.nombreProducto ?? ""),
    categoriaNombre: toNullableString(obj?.categoriaNombre),
    imagenUrl: toNullableString(obj?.imagenUrl),
    talla: toNullableString(obj?.talla),
    colorHex: toNullableString(obj?.colorHex),
    precioVenta: Number(obj?.precioVenta ?? 0),
    stock: Number(obj?.stock ?? 0),
  };
}

export function normalizeVariantesResponse(raw: unknown, p: number, pageSize: number): NormalizedVariantes {
  if (Array.isArray(raw)) {
    const list = raw.map(normalizeVariante);
    return { list, total: 0, hasNext: list.length > 0 };
  }

  const obj = asRecord(raw);
  const totalNum = Number(obj?.total ?? 0) || 0;
  const listFromApi = Array.isArray(obj?.items) ? obj.items : [];
  const list = listFromApi.map(normalizeVariante);

  const hasNext =
    obj?.hasNext !== undefined
      ? Boolean(obj.hasNext)
      : totalNum > 0
      ? p * pageSize < totalNum
      : list.length > 0;

  return { list, total: totalNum, hasNext };
}

export function makeBuscarVariantesParams(key: string, page: number, pageSize: number): BuscarVariantesParams {
  const { catId, filtro } = parseKey(key);
  return {
    filtro,
    pagina: page,
    tamanoPagina: pageSize,
    ...(catId != null ? { categoriaId: catId } : {}),
  };
}
