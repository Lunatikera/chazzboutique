import { AnimatePresence, motion } from "framer-motion";
import type { ProductoLite, VarianteLookup } from "../../../api/pos";
import { easeOut } from "../motion";
import { money } from "../utils";

type VentaLookupFormProps = {
  modoNombre: boolean;
  codigo: string;
  nombre: string;
  precio: number;
  color: string;
  cantidadStr: string;
  loadingLookup: boolean;
  loadingPay: boolean;
  lookupError: string | null;
  variante: VarianteLookup | null;
  nameQuery: string;
  nameOptions: ProductoLite[];
  nameOpen: boolean;
  loadingName: boolean;
  nameError: string | null;
  onCodigoChange: (value: string) => void;
  onBuscarCodigo: () => void;
  onNameQueryChange: (value: string) => void;
  onNameFocus: () => void;
  onNameBlur: () => void;
  onNameEnter: () => void;
  onNameEscape: () => void;
  onToggleModoNombre: (value: boolean) => void;
  onPickProducto: (producto: ProductoLite) => void;
  onCantidadChange: (value: string) => void;
  onCantidadBlur: () => void;
  onCantidadEnter: (input: HTMLInputElement) => void;
  onCantidadDec: () => void;
  onCantidadInc: () => void;
  onAgregar: () => void;
};

export default function VentaLookupForm({
  modoNombre,
  codigo,
  nombre,
  precio,
  color,
  cantidadStr,
  loadingLookup,
  loadingPay,
  lookupError,
  variante,
  nameQuery,
  nameOptions,
  nameOpen,
  loadingName,
  nameError,
  onCodigoChange,
  onBuscarCodigo,
  onNameQueryChange,
  onNameFocus,
  onNameBlur,
  onNameEnter,
  onNameEscape,
  onToggleModoNombre,
  onPickProducto,
  onCantidadChange,
  onCantidadBlur,
  onCantidadEnter,
  onCantidadDec,
  onCantidadInc,
  onAgregar,
}: VentaLookupFormProps) {
  return (
    <section className="venta__top">
      <motion.div
        className="card card--lift"
        initial={{ opacity: 0, y: 10, filter: "blur(8px)" }}
        animate={{ opacity: 1, y: 0, filter: "blur(0px)" }}
        transition={{ duration: 0.45, ease: easeOut }}
      >
        <div className="formgrid">
          <div className="field">
            <label>C&oacute;digo</label>
            <input
              value={codigo}
              onChange={(e) => onCodigoChange(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") onBuscarCodigo();
              }}
              placeholder="Escanea / escribe y presiona Enter"
              disabled={modoNombre || loadingLookup || loadingPay}
            />

            <div className={`collapse ${modoNombre ? "isClosed" : "isOpen"}`}>
              <div className="muted" style={{ marginTop: 6 }}>
                Tip: escribe/escanea y presiona <b>Enter</b> para buscar.
              </div>
            </div>

            {lookupError && !modoNombre && (
              <div className="muted danger" style={{ marginTop: 6 }}>
                {lookupError}
              </div>
            )}
          </div>

          <div className={`field field--withPopover ${modoNombre && nameOpen ? "is-popoverOpen" : ""}`}>
            <label>Nombre</label>

            <input
              className={!modoNombre ? "inputLikeDisabled" : ""}
              value={modoNombre ? nameQuery : nombre}
              readOnly={!modoNombre}
              onChange={(e) => {
                if (!modoNombre) return;
                onNameQueryChange(e.target.value);
              }}
              onFocus={onNameFocus}
              onBlur={onNameBlur}
              onKeyDown={(e) => {
                if (!modoNombre) return;
                if (e.key === "Enter") onNameEnter();
                if (e.key === "Escape") onNameEscape();
              }}
              placeholder={modoNombre ? "Escribe nombre y presiona Enter" : "Buscar por nombre (habilita toggle)"}
              disabled={loadingPay}
            />

            <div className="toggle toggle--below">
              <input
                id="toggleNombre"
                type="checkbox"
                checked={modoNombre}
                onChange={(e) => onToggleModoNombre(e.target.checked)}
              />
              <label htmlFor="toggleNombre">Habilitar b&uacute;squeda por nombre</label>
            </div>

            {modoNombre && (
              <>
                {nameError && (
                  <div className="muted danger" style={{ marginTop: 6 }}>
                    {nameError}
                  </div>
                )}

                <AnimatePresence>
                  {nameOpen && nameOptions.length > 0 && (
                    <motion.div
                      className="popover"
                      initial={{ opacity: 0, y: 8, scale: 0.99, filter: "blur(8px)" }}
                      animate={{ opacity: 1, y: 0, scale: 1, filter: "blur(0px)" }}
                      exit={{ opacity: 0, y: 6, scale: 0.99, filter: "blur(8px)" }}
                      transition={{ duration: 0.16, ease: easeOut }}
                      style={{
                        position: "absolute",
                        left: 0,
                        right: 0,
                        top: "100%",
                        marginTop: 10,
                        padding: 8,
                        maxHeight: 280,
                        overflow: "auto",
                      }}
                    >
                      <div className="popover__title">Resultados</div>

                      {loadingName && <div className="muted" style={{ padding: "10px 12px" }}>Buscando...</div>}

                      {nameOptions.map((p) => (
                        <button
                          key={p.id}
                          type="button"
                          className="rowpick"
                          onMouseDown={(ev) => ev.preventDefault()}
                          onClick={() => onPickProducto(p)}
                        >
                          <span className="rowpick__main">{p.nombreProducto}</span>
                          <span className="rowpick__hint">Enter para seleccionar</span>
                        </button>
                      ))}
                    </motion.div>
                  )}
                </AnimatePresence>
              </>
            )}
          </div>

          <div className="field">
            <label>Precio</label>
            <input value={money(precio)} readOnly />
          </div>

          <div className="field field--tiny">
            <label>Color</label>
            <div className="swatch" style={{ background: color }} />
          </div>

          <div className="field field--tiny">
            <label>Cantidad</label>
            <div className="stepper stepper--pro">
              <button type="button" onClick={onCantidadDec} disabled={loadingPay}>
                -
              </button>

              <input
                inputMode="numeric"
                value={cantidadStr}
                onFocus={(e) => e.currentTarget.select()}
                onChange={(e) => onCantidadChange(e.target.value)}
                onBlur={onCantidadBlur}
                onKeyDown={(e) => {
                  if (e.key === "Enter") onCantidadEnter(e.currentTarget);
                }}
                disabled={loadingPay}
              />

              <button type="button" onClick={onCantidadInc} disabled={loadingPay}>
                +
              </button>
            </div>
          </div>

          <div className="field field--actions">
            <label>&nbsp;</label>
            <motion.button
              className="btn btn-primary"
              type="button"
              onClick={onAgregar}
              disabled={loadingPay || !variante}
              title={!variante ? "Carga una variante (c\u00f3digo o nombre)" : undefined}
              whileHover={!loadingPay && variante ? { y: -1 } : undefined}
              whileTap={!loadingPay && variante ? { y: 0 } : undefined}
              transition={{ duration: 0.14 }}
            >
              Agregar
            </motion.button>
          </div>
        </div>
      </motion.div>
    </section>
  );
}
