export function money(n: number) {
  return n.toLocaleString("es-MX", { style: "currency", currency: "MXN" });
}

export function parseIntSafe(raw: string, fallback: number) {
  const s = raw.trim();
  if (s === "") return fallback;
  const n = Number(s);
  if (!Number.isFinite(n)) return fallback;
  return Math.trunc(n);
}

export function onlyDigits(raw: string) {
  return raw.replace(/[^\d]/g, "");
}

export function dropRecordKey<TValue>(record: Record<string, TValue>, key: string) {
  return Object.fromEntries(Object.entries(record).filter(([entryKey]) => entryKey !== key)) as Record<string, TValue>;
}
