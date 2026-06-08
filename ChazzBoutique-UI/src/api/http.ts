const API_BASE =
  import.meta.env.VITE_API_BASE_URL ||
  (import.meta.env.DEV ? "http://localhost:8080" : "");

type HttpMethod = "GET" | "POST" | "PUT" | "DELETE";

async function readBody(res: Response) {
  const text = await res.text().catch(() => "");
  if (!text) return { text: "", json: null as unknown };
  try {
    return { text, json: JSON.parse(text) };
  } catch {
    return { text, json: null as unknown };
  }
}

function asRecord(value: unknown): Record<string, unknown> | null {
  if (!value || typeof value !== "object") return null;
  return value as Record<string, unknown>;
}

function errorMessage(status: number, body: { text: string; json: unknown }) {
  const j = asRecord(body.json);
  const msg =
    j?.message ||
    j?.error ||
    j?.msg ||
    j?.detail ||
    (typeof body.json === "string" ? body.json : "") ||
    body.text;

  if (msg && String(msg).trim()) return String(msg);

  if (status === 404) return "No encontrado";
  if (status === 400) return "Solicitud inválida";
  if (status === 401) return "No autorizado";
  if (status === 403) return "Sin permisos";
  return `HTTP ${status}`;
}

async function request<T>(
  path: string,
  options: {
    method?: HttpMethod;
    body?: unknown;
    headers?: Record<string, string>;
  } = {}
): Promise<T> {
  const url = `${API_BASE}${path}`;

  const res = await fetch(url, {
    method: options.method ?? "GET",
    headers: {
      "Content-Type": "application/json",
      ...(options.headers ?? {}),
    },
    body: options.body === undefined ? undefined : JSON.stringify(options.body),
  });

  if (res.status === 204) return undefined as T;

  const body = await readBody(res);

  if (!res.ok) {
    throw new Error(errorMessage(res.status, body));
  }

  return (body.json !== null ? body.json : body.text) as T;
}

function buildQuery(params?: Record<string, unknown>) {
  if (!params) return "";
  const qs = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v === undefined || v === null) return;
    qs.set(k, String(v));
  });
  const s = qs.toString();
  return s ? `?${s}` : "";
}

export const http = {
  get: <T>(path: string, params?: Record<string, unknown>) =>
    request<T>(`${path}${buildQuery(params)}`),

  post: <T>(path: string, body: unknown) =>
    request<T>(path, { method: "POST", body }),

  put: <T>(path: string, body: unknown) =>
    request<T>(path, { method: "PUT", body }),

  del: <T>(path: string) =>
    request<T>(path, { method: "DELETE" }),
};
