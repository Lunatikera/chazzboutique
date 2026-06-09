import "../../styles/venta.css";
import MotionToast from "../../components/ui/MotionToast";
import PaymentPanel from "./components/PaymentPanel";
import SuccessModal from "./components/SuccessModal";
import VentaHeader from "./components/VentaHeader";
import VentaLookupForm from "./components/VentaLookupForm";
import VentaProductsTable from "./components/VentaProductsTable";
import VariantesModal from "./components/VariantesModal";
import { useVenta } from "./useVenta";

export default function VentaFeature() {
  const { state, refs, actions } = useVenta();

  return (
    <div className="venta">
      <VentaHeader total={state.total} />

      <VentaLookupForm
        modoNombre={state.modoNombre}
        codigo={state.codigo}
        nombre={state.nombre}
        precio={state.precio}
        color={state.color}
        cantidadStr={state.cantidadStr}
        loadingLookup={state.loadingLookup}
        loadingPay={state.loadingPay}
        lookupError={state.lookupError}
        variante={state.variante}
        nameQuery={state.nameQuery}
        nameOptions={state.nameOptions}
        nameOpen={state.nameOpen}
        loadingName={state.loadingName}
        nameError={state.nameError}
        onCodigoChange={actions.setCodigo}
        onBuscarCodigo={actions.onBuscarCodigo}
        onNameQueryChange={(value) => {
          actions.setNameQuery(value);
          actions.setNameOpen(true);
        }}
        onNameFocus={() => {
          if (state.modoNombre && state.nameOptions.length > 0) actions.setNameOpen(true);
        }}
        onNameBlur={() => window.setTimeout(() => actions.setNameOpen(false), 120)}
        onNameEnter={actions.onEnterNombre}
        onNameEscape={() => actions.setNameOpen(false)}
        onToggleModoNombre={actions.onToggleModoNombre}
        onPickProducto={actions.openVariantesForProducto}
        onCantidadChange={(raw) => {
          if (/^\d*$/.test(raw)) actions.setCantidadStr(raw);
        }}
        onCantidadBlur={() => {
          const next = Math.max(1, actions.parseIntSafe(state.cantidadStr, 1));
          actions.setCantidadStr(String(next));
        }}
        onCantidadEnter={(input) => {
          const next = Math.max(1, actions.parseIntSafe(state.cantidadStr, 1));
          actions.setCantidadStr(String(next));
          input.blur();
        }}
        onCantidadDec={() => {
          const cur = actions.getCantidadActual();
          actions.setCantidadStr(String(Math.max(1, cur - 1)));
        }}
        onCantidadInc={() => {
          const cur = actions.getCantidadActual();
          actions.setCantidadStr(String(cur + 1));
        }}
        onAgregar={actions.onAgregar}
      />

      <section className="venta__body">
        <VentaProductsTable
          rows={state.rows}
          qtyDrafts={state.qtyDrafts}
          itemsCount={state.itemsCount}
          subtotal={state.subtotal}
          loadingPay={state.loadingPay}
          onEliminar={actions.onEliminar}
          onQtyDraftChange={(id, raw) => {
            const cleaned = actions.onlyDigits(raw);
            actions.setQtyDrafts((prev) => ({ ...prev, [id]: cleaned }));
          }}
          onQtyBlur={(row) => {
            const raw = state.qtyDrafts[row.id] ?? String(row.cantidad);
            const next = Math.max(1, actions.parseIntSafe(raw, row.cantidad));

            actions.setRows((prev) => prev.map((x) => (x.id === row.id ? { ...x, cantidad: next } : x)));
            actions.setQtyDrafts((prev) => actions.dropRecordKey(prev, row.id));
          }}
        />

        <PaymentPanel
          rowsCount={state.rows.length}
          loadingPay={state.loadingPay}
          descuentoOn={state.descuentoOn}
          descuentoStr={state.descuentoStr}
          montoPagoStr={state.montoPagoStr}
          cambio={state.cambio}
          total={state.total}
          descuentoRef={refs.descuentoRef}
          onToggleDescuento={() => {
            actions.setDescuentoOn((prev) => {
              const next = !prev;
              if (!next) actions.setDescuentoStr("0");
              else window.setTimeout(() => refs.descuentoRef.current?.focus(), 0);
              return next;
            });
          }}
          onDescuentoChange={(raw) => {
            if (/^\d*$/.test(raw)) actions.setDescuentoStr(raw);
          }}
          onDescuentoBlur={() => {
            const next = Math.max(0, actions.parseIntSafe(state.descuentoStr, 0));
            actions.setDescuentoStr(String(next));
          }}
          onMontoPagoChange={(raw) => {
            if (/^\d*$/.test(raw)) actions.setMontoPagoStr(raw);
          }}
          onMontoPagoBlur={() => {
            const next = Math.max(0, actions.parseIntSafe(state.montoPagoStr, 0));
            actions.setMontoPagoStr(String(next));
          }}
          onVaciar={actions.onVaciar}
          onPagar={actions.onPagar}
        />
      </section>

      <MotionToast toast={state.toast} baseClassName="toast" />

      <SuccessModal
        open={state.successOpen}
        venta={state.ventaOk}
        onOpenTicket={actions.handleOpenTicket}
        onNewSale={actions.handleNewSale}
      />

      <VariantesModal
        open={state.varModalOpen}
        title={state.pickedProducto ? state.pickedProducto.nombreProducto : "Selecciona una variante"}
        loading={state.varModalLoading}
        variantes={state.varModalItems}
        error={state.varModalError}
        onCancel={() => actions.setVarModalOpen(false)}
        onPick={actions.onPickVariante}
      />
    </div>
  );
}
