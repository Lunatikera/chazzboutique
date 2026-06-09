import { useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import type { VarianteRow } from "../../../api/pos";
import { pop } from "../motion";
import { money } from "../utils";

type VariantesModalProps = {
  open: boolean;
  title: string;
  loading: boolean;
  variantes: VarianteRow[];
  error: string | null;
  onCancel: () => void;
  onPick: (v: VarianteRow) => void;
};

function VariantesModalContent({
  title,
  loading,
  variantes,
  error,
  onCancel,
  onPick,
}: Omit<VariantesModalProps, "open">) {
  const [selected, setSelected] = useState<number>(0);
  const selectedVar = variantes[selected] ?? null;

  return (
    <motion.div
      className="modalOverlay"
      role="dialog"
      aria-modal="true"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onCancel();
      }}
      initial={{ opacity: 0 }}
      animate={{ opacity: 1, transition: { duration: 0.18 } }}
      exit={{ opacity: 0, transition: { duration: 0.14 } }}
    >
      <motion.div className="modalCard modalCard--wide" variants={pop} initial="hidden" animate="show" exit="exit">
        <div className="modalHead">
          <div className="modalBadge sel">
            <img className="sb_hangers" src="/images/hangers.png" draggable={false} />
          </div>
          <div>
            <h3 className="modalTitle">Variantes</h3>
            <div className="modalSub">{title}</div>
          </div>
        </div>

        <div className="modalBody">
          {loading && <div className="muted">Cargando variantes...</div>}
          {error && <div className="muted danger">{error}</div>}

          {!loading && !error && variantes.length === 0 && <div className="muted">No hay variantes para este producto.</div>}

          {!loading && !error && variantes.length > 0 && (
            <div className="tablewrap tablewrap--modal" style={{ maxHeight: 420, overflow: "auto" }}>
              <table className="table table--darkHead">
                <thead>
                  <tr>
                    <th style={{ textAlign: "center", width: 92 }}>COLOR</th>
                    <th>TALLA</th>
                    <th className="num">PRECIO</th>
                    <th className="num">STOCK</th>
                    <th>CODIGO</th>
                  </tr>
                </thead>
                <tbody>
                  {variantes.map((v, i) => {
                    const active = i === selected;
                    return (
                      <tr
                        key={v.codigoBarras}
                        onClick={() => setSelected(i)}
                        onDoubleClick={() => onPick(v)}
                        className={active ? "is-rowActive" : undefined}
                        style={{ cursor: "pointer" }}
                      >
                        <td style={{ textAlign: "center" }}>
                          <span className="swatch swatch--tiny" style={{ background: v.colorHex ?? "#ffffff" }} />
                        </td>
                        <td>{v.talla ?? "\u2014"}</td>
                        <td className="num">{money(v.precioVenta)}</td>
                        <td className="num">{v.stock ?? "\u2014"}</td>
                        <td>{v.codigoBarras}</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>

        <div className="modalActions">
          <button className="btn btn-outline" type="button" onClick={onCancel}>
            Cancelar
          </button>
          <button
            className="btn btn-primary"
            type="button"
            disabled={loading || variantes.length === 0 || !selectedVar}
            onClick={() => selectedVar && onPick(selectedVar)}
          >
            Elegir variante
          </button>
        </div>
      </motion.div>
    </motion.div>
  );
}

export default function VariantesModal(props: VariantesModalProps) {
  const { open, ...contentProps } = props;

  return (
    <AnimatePresence>
      {open && <VariantesModalContent key={`${contentProps.title}-${contentProps.variantes.length}`} {...contentProps} />}
    </AnimatePresence>
  );
}
