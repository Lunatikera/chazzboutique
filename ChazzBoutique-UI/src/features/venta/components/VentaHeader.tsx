import { motion } from "framer-motion";
import { fadeUp } from "../motion";
import { money } from "../utils";

type VentaHeaderProps = {
  total: number;
};

export default function VentaHeader({ total }: VentaHeaderProps) {
  return (
    <motion.header className="venta__header" variants={fadeUp} initial="hidden" animate="show">
      <div className="venta__hero">
        <div className="venta__heroText">
          <h1>Punto de Venta</h1>
          <p>Registra productos, aplica descuento y cobra en segundos.</p>
        </div>

        <div className="venta__heroMeta">
          <div className="kpi kpi--brand">
            <div className="kpi__label">Total vendido hoy</div>
            <div className="kpi__value">{money(total)}</div>
          </div>
        </div>
      </div>
    </motion.header>
  );
}
