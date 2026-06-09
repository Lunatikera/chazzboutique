import type { RefObject } from "react";
import { motion } from "framer-motion";
import { fadeUp } from "../motion";
import { money } from "../utils";

type PaymentPanelProps = {
  rowsCount: number;
  loadingPay: boolean;
  descuentoOn: boolean;
  descuentoStr: string;
  montoPagoStr: string;
  cambio: number;
  total: number;
  descuentoRef: RefObject<HTMLInputElement | null>;
  onToggleDescuento: () => void;
  onDescuentoChange: (raw: string) => void;
  onDescuentoBlur: () => void;
  onMontoPagoChange: (raw: string) => void;
  onMontoPagoBlur: () => void;
  onVaciar: () => void;
  onPagar: () => void;
};

export default function PaymentPanel({
  rowsCount,
  loadingPay,
  descuentoOn,
  descuentoStr,
  montoPagoStr,
  cambio,
  total,
  descuentoRef,
  onToggleDescuento,
  onDescuentoChange,
  onDescuentoBlur,
  onMontoPagoChange,
  onMontoPagoBlur,
  onVaciar,
  onPagar,
}: PaymentPanelProps) {
  return (
    <motion.aside className="card paycard card--lift" variants={fadeUp} initial="hidden" animate="show">
      <div className="payhead">
        <h2>Registro del Pago</h2>
      </div>

      <div className="paygrid">
        <label className="labelWithBtn">
          <span>Descuento</span>
          <button type="button" className="miniAction" disabled={rowsCount === 0 || loadingPay} onClick={onToggleDescuento}>
            {descuentoOn ? "Quitar" : "Poner"}
          </button>
        </label>

        <input
          ref={descuentoRef}
          inputMode="numeric"
          value={descuentoStr}
          onFocus={(e) => e.currentTarget.select()}
          onKeyDown={(e) => {
            if (e.key === "Enter") e.currentTarget.blur();
          }}
          onChange={(e) => onDescuentoChange(e.currentTarget.value)}
          onBlur={onDescuentoBlur}
          disabled={rowsCount === 0 || loadingPay || !descuentoOn}
        />

        <label>Monto pago</label>
        <input
          inputMode="numeric"
          value={montoPagoStr}
          onFocus={(e) => e.currentTarget.select()}
          onKeyDown={(e) => {
            if (e.key === "Enter") e.currentTarget.blur();
          }}
          onChange={(e) => onMontoPagoChange(e.currentTarget.value)}
          onBlur={onMontoPagoBlur}
          disabled={rowsCount === 0 || loadingPay}
        />

        <label>Cambio</label>
        <div className="valueBox">{money(cambio)}</div>

        <div className="payDivider" aria-hidden="true" />

        <label className="totalLabel">Total</label>
        <div className="valueBox valueBox--total">{money(total)}</div>
      </div>

      <div className="payactions">
        <button className="btn btn-outline" type="button" onClick={onVaciar} disabled={rowsCount === 0 || loadingPay}>
          Borrar productos
        </button>

        <motion.button
          className="btn btn-danger"
          type="button"
          onClick={onPagar}
          disabled={rowsCount === 0 || loadingPay}
          whileHover={!loadingPay && rowsCount > 0 ? { y: -1 } : undefined}
          whileTap={!loadingPay && rowsCount > 0 ? { y: 0 } : undefined}
          transition={{ duration: 0.14 }}
        >
          {loadingPay ? "Procesando..." : "Pagar"}
        </motion.button>
      </div>

      <div className="payhint">Enter confirma campos. Escape cierra listas. Doble click elige variante en modal.</div>
    </motion.aside>
  );
}
