import { useEffect, useMemo, useRef, useState } from "react";
import * as posApi from "../../api/pos";
import type { CrearVentaRequest, ProductoLite, VarianteLookup, VarianteRow } from "../../api/pos";
import type { VentaOk, VentaRow, VentaToast } from "./types";
import { dropRecordKey, money, onlyDigits, parseIntSafe } from "./utils";
import { useAuth } from "../../auth/AuthContext";

function getNombreVariante(v: VarianteLookup | null, fallback = ""): string {
  return v?.nombreProducto ?? fallback;
}

export function useVenta() {
  const { user } = useAuth();

  const [modoNombre, setModoNombre] = useState(false);

  const [codigo, setCodigo] = useState("");
  const [nombre, setNombre] = useState("");
  const [precio, setPrecio] = useState<number>(0);
  const [color, setColor] = useState<string>("#ffffff");

  const [cantidadStr, setCantidadStr] = useState<string>("1");
  const [rows, setRows] = useState<VentaRow[]>([]);

  const [descuentoStr, setDescuentoStr] = useState<string>("0");
  const [montoPagoStr, setMontoPagoStr] = useState<string>("0");

  const descuento = useMemo(() => Math.max(0, parseIntSafe(descuentoStr, 0)), [descuentoStr]);
  const montoPago = useMemo(() => Math.max(0, parseIntSafe(montoPagoStr, 0)), [montoPagoStr]);

  const [loadingLookup, setLoadingLookup] = useState(false);
  const [lookupError, setLookupError] = useState<string | null>(null);
  const [variante, setVariante] = useState<VarianteLookup | null>(null);

  const [loadingPay, setLoadingPay] = useState(false);

  const [toast, setToast] = useState<VentaToast>(null);
  const toastTimer = useRef<number | null>(null);

  const [successOpen, setSuccessOpen] = useState(false);
  const [ventaOk, setVentaOk] = useState<VentaOk | null>(null);
  const [ticketUrl, setTicketUrl] = useState("");

  const [nameQuery, setNameQuery] = useState("");
  const [nameOptions, setNameOptions] = useState<ProductoLite[]>([]);
  const [nameOpen, setNameOpen] = useState(false);
  const [loadingName, setLoadingName] = useState(false);
  const [nameError, setNameError] = useState<string | null>(null);
  const [pickedProducto, setPickedProducto] = useState<ProductoLite | null>(null);

  const [varModalOpen, setVarModalOpen] = useState(false);
  const [varModalLoading, setVarModalLoading] = useState(false);
  const [varModalError, setVarModalError] = useState<string | null>(null);
  const [varModalItems, setVarModalItems] = useState<VarianteRow[]>([]);

  const [qtyDrafts, setQtyDrafts] = useState<Record<string, string>>({});

  const subtotal = useMemo(() => rows.reduce((acc, r) => acc + r.precio * r.cantidad, 0), [rows]);
  const total = useMemo(() => Math.max(0, subtotal - descuento), [subtotal, descuento]);
  const cambio = useMemo(() => Math.max(0, montoPago - total), [montoPago, total]);
  const [descuentoOn, setDescuentoOn] = useState(false);
  const descuentoRef = useRef<HTMLInputElement | null>(null);

  const itemsCount = useMemo(() => {
    return rows.reduce((acc, r) => {
      const draft = qtyDrafts[r.id];
      const qty = Math.max(1, parseIntSafe(draft ?? String(r.cantidad), r.cantidad));
      return acc + qty;
    }, 0);
  }, [rows, qtyDrafts]);

  function showToast(type: "error" | "ok", text: string) {
    setToast({ type, text });
    if (toastTimer.current) window.clearTimeout(toastTimer.current);
    toastTimer.current = window.setTimeout(() => setToast(null), 2300);
  }

  function resetLookup() {
    setCodigo("");
    setNombre("");
    setPrecio(0);
    setColor("#ffffff");
    setCantidadStr("1");
    setVariante(null);
    setLookupError(null);
    setPickedProducto(null);
    setNameQuery("");
    setNameOptions([]);
    setNameOpen(false);
  }

  function getCantidadActual() {
    return Math.max(1, parseIntSafe(cantidadStr, 1));
  }

  async function onBuscarCodigo() {
    const c = codigo.trim();
    if (!c) return;

    setLoadingLookup(true);
    setLookupError(null);

    try {
      const v = await posApi.buscarVariantePorCodigo(c);
      setVariante(v);

      const nombreV = getNombreVariante(v, "");
      setNombre(nombreV);
      setPrecio(v.precioVenta);
      setColor(v.colorHex ?? "#ffffff");

      setNameQuery(nombreV);
      setPickedProducto(null);
    } catch (e) {
      setVariante(null);
      setNombre("");
      setPrecio(0);
      setColor("#ffffff");
      showToast("error", (e as Error).message || "No encontrado");
    } finally {
      setLoadingLookup(false);
    }
  }

  async function openVariantesForProducto(p: ProductoLite) {
    setPickedProducto(p);
    setNameQuery(p.nombreProducto);
    setNameOpen(false);

    setVarModalOpen(true);
    setVarModalLoading(true);
    setVarModalError(null);
    setVarModalItems([]);

    try {
      const vars = await posApi.obtenerVariantesPorProducto(p.id);
      setVarModalItems(vars ?? []);
      if (!vars || vars.length === 0) setVarModalError("Este producto no tiene variantes.");
    } catch (e) {
      setVarModalError((e as Error).message || "Error cargando variantes");
    } finally {
      setVarModalLoading(false);
    }
  }

  async function onPickVariante(v: VarianteRow) {
    setVarModalOpen(false);
    setLoadingLookup(true);
    setLookupError(null);

    try {
      const full = await posApi.buscarVariantePorCodigo(v.codigoBarras);
      setVariante(full);

      const nombreV = getNombreVariante(full, nameQuery ?? "");
      setNombre(nombreV);
      setPrecio(full.precioVenta);
      setColor(full.colorHex ?? "#ffffff");
      setCodigo(full.codigoBarras);

      showToast("ok", "Variante cargada");
    } catch (e) {
      const msg = (e as Error).message || "Error cargando variante";
      setLookupError(msg);
      showToast("error", msg);
    } finally {
      setLoadingLookup(false);
    }
  }

  async function onEnterNombre() {
    if (nameOptions.length === 1) {
      await openVariantesForProducto(nameOptions[0]);
      return;
    }

    if (pickedProducto) {
      await openVariantesForProducto(pickedProducto);
      return;
    }

    setNameOpen(true);
    if (nameOptions.length === 0) showToast("error", "No hay coincidencias para ese nombre.");
  }

  function onToggleModoNombre(value: boolean) {
    setModoNombre(value);
    resetLookup();
    setNameError(null);
  }

  function onAgregar() {
    if (!variante) {
      showToast(
        "error",
        modoNombre
          ? "Primero elige una variante (Enter en nombre / seleccionar opci\u00f3n)."
          : "Primero busca un c\u00f3digo v\u00e1lido (Enter) para cargar el producto."
      );
      return;
    }

    const cant = getCantidadActual();
    const nombreSeguro = variante.nombreProducto || nombre || nameQuery || "Producto";

    setRows((prev) => {
      const idx = prev.findIndex((r) => r.codigoBarras === variante.codigoBarras);
      if (idx !== -1) {
        const copy = [...prev];
        copy[idx] = {
          ...copy[idx],
          cantidad: copy[idx].cantidad + cant,
          nombre: copy[idx].nombre || nombreSeguro,
        };
        return copy;
      }

      return [
        ...prev,
        {
          id: crypto.randomUUID(),
          codigoBarras: variante.codigoBarras,
          nombre: nombreSeguro,
          cantidad: cant,
          precio: Math.max(0, variante.precioVenta),
          colorHex: variante.colorHex,
        },
      ];
    });

    resetLookup();
    showToast("ok", "Producto agregado");
  }

  function onEliminar(id: string) {
    setRows((prev) => prev.filter((r) => r.id !== id));
    setQtyDrafts((prev) => dropRecordKey(prev, id));
  }

  function onVaciar() {
    setRows([]);
    setQtyDrafts({});
    setDescuentoStr("0");
    setMontoPagoStr("0");
    resetLookup();
    setNameError(null);
    setDescuentoOn(false);
  }

  async function onPagar() {
    if (rows.length === 0) return;

    if (!user) {
      showToast("error", "Sesión no válida. Inicia sesión de nuevo.");
      return;
    }

    if (montoPago < total) {
      showToast("error", `Pago insuficiente. Falta: ${money(total - montoPago)}`);
      return;
    }

    setLoadingPay(true);
    try {
      const payload: CrearVentaRequest = {
        usuarioId: user.usuarioId,
        descuento,
        montoPago,
        detalles: rows.map((r) => ({ codigoBarras: r.codigoBarras, cantidad: r.cantidad })),
      };

      const res = await posApi.crearVenta(payload);
      const url = posApi.ticketPdfUrl(res.id);

      window.open(url, "_blank", "noopener,noreferrer");

      setVentaOk({ id: res.id, total: res.total, cambio: res.cambio });
      setTicketUrl(url);
      setSuccessOpen(true);
    } catch (e) {
      showToast("error", `Error al pagar: ${(e as Error).message}`);
    } finally {
      setLoadingPay(false);
    }
  }

  function handleOpenTicket() {
    if (ticketUrl) window.open(ticketUrl, "_blank", "noopener,noreferrer");
  }

  function handleNewSale() {
    setSuccessOpen(false);
    setVentaOk(null);
    setTicketUrl("");
    onVaciar();
  }

  useEffect(() => {
    return () => {
      if (toastTimer.current) window.clearTimeout(toastTimer.current);
    };
  }, []);

  useEffect(() => {
    if (!modoNombre) return;

    const q = nameQuery.trim();
    setNameError(null);
    setPickedProducto(null);

    if (q.length < 2) {
      setNameOptions([]);
      setNameOpen(false);
      return;
    }

    setLoadingName(true);
    const t = window.setTimeout(async () => {
      try {
        const items = await posApi.buscarProductosPorNombre(q);
        setNameOptions(items ?? []);
        setNameOpen(true);
      } catch (e) {
        setNameOptions([]);
        setNameOpen(false);
        setNameError((e as Error).message || "Error buscando por nombre");
      } finally {
        setLoadingName(false);
      }
    }, 250);

    return () => window.clearTimeout(t);
  }, [nameQuery, modoNombre]);

  return {
    state: {
      modoNombre,
      codigo,
      nombre,
      precio,
      color,
      cantidadStr,
      rows,
      descuentoStr,
      montoPagoStr,
      loadingLookup,
      lookupError,
      variante,
      loadingPay,
      toast,
      successOpen,
      ventaOk,
      nameQuery,
      nameOptions,
      nameOpen,
      loadingName,
      nameError,
      pickedProducto,
      varModalOpen,
      varModalLoading,
      varModalError,
      varModalItems,
      qtyDrafts,
      subtotal,
      total,
      cambio,
      descuentoOn,
      itemsCount,
    },
    refs: {
      descuentoRef,
    },
    actions: {
      setCodigo,
      setNameQuery,
      setNameOpen,
      setCantidadStr,
      setDescuentoStr,
      setMontoPagoStr,
      setQtyDrafts,
      setRows,
      setVarModalOpen,
      onBuscarCodigo,
      openVariantesForProducto,
      onPickVariante,
      onEnterNombre,
      onToggleModoNombre,
      onAgregar,
      onEliminar,
      onVaciar,
      onPagar,
      handleOpenTicket,
      handleNewSale,
      getCantidadActual,
      showToast,
      onlyDigits,
      parseIntSafe,
      dropRecordKey,
      setDescuentoOn,
    },
  };
}
