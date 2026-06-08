import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import * as catalogoApi from "../../api/catalogo";
import type { Categoria } from "../../api/catalogo";
import { PAGE_SIZE } from "./constants";
import {
  clamp,
  makeBuscarVariantesParams,
  makeKey,
  normalizeVariantesResponse,
  type NormalizedVariantes,
} from "./catalogoUtils";
import type { CatalogoToast, FeedState } from "./types";

function getMaxScroll(rail: HTMLDivElement) {
  return Math.max(0, rail.scrollWidth - rail.clientWidth);
}

export function useCatalogo(reduceMotion: boolean | null) {
  const [toast, setToast] = useState<CatalogoToast>(null);
  const toastTimer = useRef<number | null>(null);

  const [cats, setCats] = useState<Categoria[]>([]);
  const [catsLoading, setCatsLoading] = useState(false);
  const [catsError, setCatsError] = useState<string | null>(null);
  const [catId, setCatId] = useState<number | null>(null);

  const [filtro, setFiltro] = useState("");
  const [filtroAplicado, setFiltroAplicado] = useState("");
  const searchInputRef = useRef<HTMLInputElement | null>(null);

  const catRailRef = useRef<HTMLDivElement | null>(null);
  const catStepRef = useRef(0);
  const [catEndPadPx, setCatEndPadPx] = useState(0);

  const [feed, setFeed] = useState<FeedState>(() => ({
    key: makeKey(null, ""),
    items: [],
    total: 0,
    hasMore: true,
    page: 0,
  }));

  const requestSeq = useRef(0);
  const loadingRef = useRef(false);
  const seenIdsRef = useRef<Set<number>>(new Set());
  const [err, setErr] = useState<string | null>(null);

  const [switching, setSwitching] = useState(false);
  const [overlayOn, setOverlayOn] = useState(false);
  const overlayTimer = useRef<number | null>(null);

  const [bootLoading, setBootLoading] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);
  const sentinelRef = useRef<HTMLDivElement | null>(null);

  const stageRef = useRef<HTMLDivElement | null>(null);
  const [stageMinH, setStageMinH] = useState<number | null>(null);
  const preserveScrollRef = useRef(0);

  const [slowFetch, setSlowFetch] = useState(false);
  const slowTimer = useRef<number | null>(null);
  const smoothRestoreTimer = useRef<number | null>(null);

  const activeKey = useMemo(() => makeKey(catId, filtroAplicado), [catId, filtroAplicado]);

  const uiCatName = useMemo(() => {
    if (!catId) return "Todas";
    return cats.find((c) => c.id === catId)?.nombre ?? "Categoria";
  }, [catId, cats]);

  function showToast(type: "ok" | "error", text: string) {
    setToast({ type, text });
    if (toastTimer.current) window.clearTimeout(toastTimer.current);
    toastTimer.current = window.setTimeout(() => setToast(null), 2200);
  }

  const fetchPageForKey = useCallback(async (key: string, page: number): Promise<NormalizedVariantes> => {
    const params = makeBuscarVariantesParams(key, page, PAGE_SIZE);
    const raw = await catalogoApi.buscarVariantes(params);
    return normalizeVariantesResponse(raw, page, PAGE_SIZE);
  }, []);

  function lockStageHeight() {
    const el = stageRef.current;
    if (!el) return;
    const h = Math.round(el.getBoundingClientRect().height);
    if (h > 0) setStageMinH(h);
  }

  function unlockStageHeight() {
    setStageMinH(null);
  }

  function measureCatStepAndPad() {
    const rail = catRailRef.current;
    if (!rail) return;

    const first = rail.querySelector<HTMLElement>(".nfTile");
    if (!first) return;

    const second = first.nextElementSibling as HTMLElement | null;
    if (second) {
      const r1 = first.getBoundingClientRect();
      const r2 = second.getBoundingClientRect();
      const step = Math.round(r2.left - r1.left);
      if (step > 0) catStepRef.current = step;
    } else {
      catStepRef.current = Math.round(first.getBoundingClientRect().width);
    }

    const cs = getComputedStyle(rail);
    const pl = parseFloat(cs.paddingLeft || "0") || 0;
    const pr = parseFloat(cs.paddingRight || "0") || 0;
    const tileW = Math.round(first.getBoundingClientRect().width);
    const endPad = Math.max(0, Math.floor(rail.clientWidth - tileW - pl - pr));
    setCatEndPadPx(endPad);
  }

  function scrollCatsByOne(dir: -1 | 1) {
    const rail = catRailRef.current;
    if (!rail) return;
    const step = catStepRef.current || 260;
    const max = getMaxScroll(rail);
    const target = clamp(rail.scrollLeft + dir * step, 0, max);
    rail.scrollTo({ left: target, behavior: reduceMotion ? "auto" : "smooth" });
  }

  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if (e.key.toLowerCase() !== "k") return;
      if (!(e.ctrlKey || e.metaKey)) return;
      e.preventDefault();
      searchInputRef.current?.focus();
    };
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, []);

  useEffect(() => {
    const t = window.setTimeout(() => setFiltroAplicado(filtro.trim()), 250);
    return () => window.clearTimeout(t);
  }, [filtro]);

  useEffect(() => {
    return () => {
      if (toastTimer.current) window.clearTimeout(toastTimer.current);
      if (overlayTimer.current) window.clearTimeout(overlayTimer.current);
      if (slowTimer.current) window.clearTimeout(slowTimer.current);
      if (smoothRestoreTimer.current) window.clearTimeout(smoothRestoreTimer.current);
    };
  }, []);

  useEffect(() => {
    let alive = true;
    setCatsLoading(true);
    setCatsError(null);

    catalogoApi
      .listarCategorias()
      .then((res) => {
        if (!alive) return;
        setCats(res ?? []);
      })
      .catch((e) => {
        if (!alive) return;
        setCats([]);
        setCatsError((e as Error).message || "Error cargando categorias");
      })
      .finally(() => {
        if (!alive) return;
        setCatsLoading(false);
      });

    return () => {
      alive = false;
    };
  }, []);

  useEffect(() => {
    if (!cats.length) return;
    const raf = requestAnimationFrame(() => measureCatStepAndPad());
    const onResize = () => measureCatStepAndPad();
    window.addEventListener("resize", onResize);
    return () => {
      cancelAnimationFrame(raf);
      window.removeEventListener("resize", onResize);
    };
  }, [cats]);

  useEffect(() => {
    const rail = catRailRef.current;
    if (!rail) return;

    const onWheel = (e: WheelEvent) => {
      const absX = Math.abs(e.deltaX);
      const absY = Math.abs(e.deltaY);
      if (absX > absY) return;
      e.preventDefault();
      const max = getMaxScroll(rail);
      rail.scrollLeft = clamp(rail.scrollLeft + e.deltaY, 0, max);
    };

    rail.addEventListener("wheel", onWheel, { passive: false });
    return () => rail.removeEventListener("wheel", onWheel);
  }, []);

  useEffect(() => {
    if (feed.page > 0 || bootLoading) return;

    const seq = ++requestSeq.current;
    loadingRef.current = true;
    setBootLoading(true);
    setErr(null);
    setSlowFetch(false);

    if (slowTimer.current) window.clearTimeout(slowTimer.current);
    slowTimer.current = window.setTimeout(() => setSlowFetch(true), reduceMotion ? 0 : 220);

    (async () => {
      try {
        const norm = await fetchPageForKey(activeKey, 1);
        if (seq !== requestSeq.current) return;

        const s = new Set<number>();
        for (const it of norm.list) s.add(it.varianteId);
        seenIdsRef.current = s;

        setFeed({
          key: activeKey,
          items: norm.list,
          total: norm.total,
          hasMore: norm.hasNext,
          page: 1,
        });
      } catch (e) {
        if (seq !== requestSeq.current) return;
        setErr((e as Error).message || "Error cargando catalogo");
      } finally {
        if (seq === requestSeq.current) {
          setBootLoading(false);
          loadingRef.current = false;
          setSlowFetch(false);
          if (slowTimer.current) window.clearTimeout(slowTimer.current);
        }
      }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (activeKey === feed.key) return;

    preserveScrollRef.current = window.scrollY || 0;
    lockStageHeight();

    setErr(null);
    setSwitching(true);
    setOverlayOn(false);

    if (overlayTimer.current) window.clearTimeout(overlayTimer.current);
    overlayTimer.current = window.setTimeout(() => {
      setOverlayOn(true);
    }, reduceMotion ? 0 : 140);

    setSlowFetch(false);
    if (slowTimer.current) window.clearTimeout(slowTimer.current);
    if (feed.items.length === 0) {
      slowTimer.current = window.setTimeout(() => setSlowFetch(true), reduceMotion ? 0 : 220);
    }

    const seq = ++requestSeq.current;
    loadingRef.current = true;

    (async () => {
      try {
        const norm = await fetchPageForKey(activeKey, 1);
        if (seq !== requestSeq.current) return;

        const s = new Set<number>();
        for (const it of norm.list) s.add(it.varianteId);
        seenIdsRef.current = s;

        setFeed({
          key: activeKey,
          items: norm.list,
          total: norm.total,
          hasMore: norm.hasNext,
          page: 1,
        });

        requestAnimationFrame(() => {
          window.scrollTo({ top: preserveScrollRef.current, left: 0, behavior: "auto" });
        });

        if (!reduceMotion && norm.list.length === 0) {
          if (smoothRestoreTimer.current) window.clearTimeout(smoothRestoreTimer.current);
          smoothRestoreTimer.current = window.setTimeout(() => {
            window.scrollTo({ top: preserveScrollRef.current, left: 0, behavior: "smooth" });
          }, 60);
        }
      } catch (e) {
        if (seq !== requestSeq.current) return;
        setErr((e as Error).message || "Error cargando catalogo");
      } finally {
        if (seq === requestSeq.current) {
          setSwitching(false);
          setOverlayOn(false);
          loadingRef.current = false;
          unlockStageHeight();
          setSlowFetch(false);
          if (overlayTimer.current) window.clearTimeout(overlayTimer.current);
          if (slowTimer.current) window.clearTimeout(slowTimer.current);
          if (smoothRestoreTimer.current) window.clearTimeout(smoothRestoreTimer.current);
        }
      }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeKey]);

  const loadMore = useCallback(async () => {
    if (loadingRef.current) return;
    if (switching) return;
    if (!feed.hasMore) return;

    loadingRef.current = true;
    setLoadingMore(true);
    setErr(null);

    const seq = ++requestSeq.current;
    const nextPage = Math.max(1, feed.page + 1);
    const key = feed.key;

    try {
      const norm = await fetchPageForKey(key, nextPage);
      if (seq !== requestSeq.current) return;

      setFeed((prev) => {
        if (prev.key !== key) return prev;

        const s = seenIdsRef.current;
        const merged = [...prev.items];
        for (const it of norm.list) {
          if (!s.has(it.varianteId)) {
            s.add(it.varianteId);
            merged.push(it);
          }
        }

        const total = norm.total > 0 ? norm.total : prev.total;

        return {
          ...prev,
          items: merged,
          total,
          hasMore: norm.hasNext && norm.list.length > 0,
          page: nextPage,
        };
      });
    } catch (e) {
      if (seq !== requestSeq.current) return;
      setErr((e as Error).message || "Error cargando mas productos");
      setFeed((prev) => ({ ...prev, hasMore: false }));
    } finally {
      if (seq === requestSeq.current) {
        setLoadingMore(false);
        loadingRef.current = false;
      }
    }
  }, [feed.hasMore, feed.key, feed.page, fetchPageForKey, switching]);

  useEffect(() => {
    const el = sentinelRef.current;
    if (!el) return;

    const obs = new IntersectionObserver(
      (entries) => {
        const entry = entries[0];
        if (!entry.isIntersecting) return;
        if (loadingRef.current) return;
        loadMore();
      },
      { root: null, rootMargin: "520px 0px 520px 0px", threshold: 0.01 }
    );

    obs.observe(el);
    return () => obs.disconnect();
  }, [loadMore]);

  return {
    state: {
      toast,
      cats,
      catsLoading,
      catsError,
      catId,
      filtro,
      catEndPadPx,
      feed,
      err,
      switching,
      overlayOn,
      bootLoading,
      loadingMore,
      stageMinH,
      slowFetch,
      uiCatName,
    },
    refs: {
      searchInputRef,
      catRailRef,
      sentinelRef,
      stageRef,
    },
    actions: {
      setCatId,
      setFiltro,
      showToast,
      scrollCatsByOne,
    },
  };
}
