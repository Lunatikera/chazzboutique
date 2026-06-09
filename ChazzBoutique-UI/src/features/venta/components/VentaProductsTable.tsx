import { motion } from "framer-motion";
import { fadeUp, easeOut } from "../motion";
import type { VentaRow } from "../types";
import { money } from "../utils";

type VentaProductsTableProps = {
  rows: VentaRow[];
  qtyDrafts: Record<string, string>;
  itemsCount: number;
  subtotal: number;
  loadingPay: boolean;
  onEliminar: (id: string) => void;
  onQtyDraftChange: (id: string, raw: string) => void;
  onQtyBlur: (row: VentaRow) => void;
};

export default function VentaProductsTable({
  rows,
  qtyDrafts,
  itemsCount,
  subtotal,
  loadingPay,
  onEliminar,
  onQtyDraftChange,
  onQtyBlur,
}: VentaProductsTableProps) {
  return (
    <motion.div className="card tablecard card--lift" variants={fadeUp} initial="hidden" animate="show">
      <div className="tablecard__head">
        <h2>Productos</h2>

        <div className="tableMeta">
          <div className="tableMeta__chip">
            <span>Items</span>
            <b>{itemsCount}</b>
          </div>
          <div className="tableMeta__chip">
            <span>Subtotal</span>
            <b className="brand">{money(subtotal)}</b>
          </div>
        </div>
      </div>

      <div className="tablewrap">
        <table className="table">
          <thead>
            <tr>
              <th>NOMBRE PRODUCTO</th>
              <th style={{ width: 92, textAlign: "center" }}>COLOR</th>
              <th className="num">CANTIDAD</th>
              <th className="num">PRECIO UNITARIO</th>
              <th className="num">SUBTOTAL</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {rows.length === 0 ? (
              <tr>
                <td colSpan={6} className="empty">
                  Agrega productos para comenzar la venta.
                </td>
              </tr>
            ) : (
              rows.map((r) => {
                const draft = qtyDrafts[r.id];
                const value = draft ?? String(r.cantidad);

                return (
                  <motion.tr
                    key={r.id}
                    className="row"
                    initial={{ opacity: 0, y: 6 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.18, ease: easeOut }}
                    whileHover={{ backgroundColor: "rgba(16,24,40,0.02)" }}
                  >
                    <td className="name">
                      <div className="name__top">
                        <span className="name__title">{r.nombre}</span>
                      </div>
                      <div className="muted" style={{ fontSize: 13, marginTop: 4 }}>
                        {r.codigoBarras}
                      </div>
                    </td>

                    <td style={{ textAlign: "center" }}>
                      {r.colorHex ? (
                        <span className="colorDot" title={r.colorHex} style={{ background: r.colorHex }} />
                      ) : (
                        <span className="muted">&mdash;</span>
                      )}
                    </td>

                    <td className="num">
                      <input
                        className="qty"
                        inputMode="numeric"
                        value={value}
                        onFocus={(e) => e.currentTarget.select()}
                        onKeyDown={(e) => {
                          if (e.key === "Enter") e.currentTarget.blur();
                        }}
                        onChange={(e) => onQtyDraftChange(r.id, e.currentTarget.value)}
                        onBlur={() => onQtyBlur(r)}
                        disabled={loadingPay}
                      />
                    </td>

                    <td className="num">{money(r.precio)}</td>
                    <td className="num">{money(r.precio * r.cantidad)}</td>

                    <td className="actions">
                      <button
                        className="iconbtn"
                        onClick={() => onEliminar(r.id)}
                        title="Eliminar"
                        disabled={loadingPay}
                        type="button"
                      >
                        &times;
                      </button>
                    </td>
                  </motion.tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>

      <div className="tablecard__foot">
        <div className="totalbar">
          <span>SUBTOTAL:</span>
          <b className="brand">{money(subtotal)}</b>
        </div>
      </div>
    </motion.div>
  );
}
