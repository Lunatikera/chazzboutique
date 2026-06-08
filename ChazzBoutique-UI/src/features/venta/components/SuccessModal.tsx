import { AnimatePresence, motion } from "framer-motion";
import { pop } from "../motion";
import type { VentaOk } from "../types";
import { money } from "../utils";

type SuccessModalProps = {
  open: boolean;
  venta: VentaOk | null;
  onOpenTicket: () => void;
  onNewSale: () => void;
};

export default function SuccessModal({ open, venta, onOpenTicket, onNewSale }: SuccessModalProps) {
  return (
    <AnimatePresence>
      {open && venta && (
        <motion.div
          className="modalOverlay"
          role="dialog"
          aria-modal="true"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1, transition: { duration: 0.18 } }}
          exit={{ opacity: 0, transition: { duration: 0.14 } }}
        >
          <motion.div className="modalCard" variants={pop} initial="hidden" animate="show" exit="exit">
            <div className="modalHead">
              <div className="modalBadge ok">
                <img className="sb_check" src="/images/check.png" draggable={false} />
              </div>
              <div>
                <h3 className="modalTitle">Venta registrada</h3>
                <div className="modalSub">Ticket generado correctamente.</div>
              </div>
            </div>

            <div className="modalBody">
              <div className="modalRow">
                <span>ID</span>
                <b>#{venta.id}</b>
              </div>
              <div className="modalRow">
                <span>Total</span>
                <b>{money(venta.total)}</b>
              </div>
              <div className="modalRow">
                <span>Cambio</span>
                <b>{money(venta.cambio)}</b>
              </div>
            </div>

            <div className="modalActions">
              <button className="btn btn-outline" type="button" onClick={onNewSale}>
                Nueva venta
              </button>
              <button className="btn btn-primary" type="button" onClick={onOpenTicket}>
                Abrir ticket
              </button>
            </div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
