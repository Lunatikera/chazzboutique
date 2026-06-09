export function imageUrl(dbPath: string) {
  if (!dbPath) return "";

  const base = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

  // si ya viene con /api/imagenes, lo normalizamos para no duplicar
  let p = dbPath.trim();
  p = p.replace(/^\/?api\/imagenes\/?/, "/");

  if (!p.startsWith("/")) p = "/" + p;

  return `${base}/api/imagenes${p}`;
}
