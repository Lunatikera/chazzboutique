import type { RefObject } from "react";
import { AnimatePresence, motion, type Variants } from "framer-motion";
import type { VarianteCatalogo } from "../../../api/catalogo";
import { PAGE_SIZE } from "../constants";
import type { FeedState } from "../types";
import { apiImgSrc, money } from "../catalogoUtils";
import { easeOut } from "../motion";

type ProductosSectionProps = {
  sectionVariants: Variants;
  reduceMotion: boolean | null;
  feed: FeedState;
  err: string | null;
  stageRef: RefObject<HTMLDivElement | null>;
  stageMinH: number | null;
  switching: boolean;
  overlayOn: boolean;
  bootLoading: boolean;
  slowFetch: boolean;
  loadingMore: boolean;
  sentinelRef: RefObject<HTMLDivElement | null>;
  onAdminProduct: () => void;
};

function ProductSkeleton() {
  return (
    <>
      {Array.from({ length: PAGE_SIZE }).map((_, i) => (
        <div key={`sk-${i}`} className="productCard skCard">
          <div className="skMedia" />
          <div className="skLine" />
          <div className="skLine skLine--short" />
        </div>
      ))}
    </>
  );
}

function LoadingOverlay({ reduceMotion }: { reduceMotion: boolean | null }) {
  return (
    <AnimatePresence>
      <motion.div
        className="gridOverlay"
        initial={reduceMotion ? false : { opacity: 0 }}
        animate={reduceMotion ? undefined : { opacity: 1 }}
        exit={reduceMotion ? undefined : { opacity: 0 }}
        transition={reduceMotion ? undefined : { duration: 0.2, ease: easeOut }}
        aria-hidden="true"
      >
        <div className="gridOverlay__dots" aria-hidden="true">
          <span className="dot" />
          <span className="dot" />
          <span className="dot" />
        </div>
      </motion.div>
    </AnimatePresence>
  );
}

function ProductCard({
  item,
  reduceMotion,
  onAdminProduct,
}: {
  item: VarianteCatalogo;
  reduceMotion: boolean | null;
  onAdminProduct: () => void;
}) {
  const src = apiImgSrc(item.imagenUrl);

  return (
    <motion.article
      key={item.varianteId}
      className="productCard pCard"
      layout="position"
      initial={reduceMotion ? false : { opacity: 0, y: 10, filter: "blur(10px)" }}
      animate={reduceMotion ? undefined : { opacity: 1, y: 0, filter: "blur(0px)" }}
      exit={reduceMotion ? undefined : { opacity: 0, y: 8, filter: "blur(10px)" }}
      transition={reduceMotion ? undefined : { duration: 0.26, ease: easeOut }}
    >
      <div className="productMedia pMedia">
        {src ? (
          <img src={src} alt={item.nombreProducto} loading="lazy" decoding="async" />
        ) : (
          <div className="thumbFallback" aria-hidden="true" />
        )}

        <div className="mediaOverlay" aria-hidden="true" />

        <div className="pChips" aria-hidden="true">
          <span className={`pChip ${item.stock > 0 ? "ok" : "danger"}`}>{item.stock > 0 ? `Stock ${item.stock}` : "Sin stock"}</span>
          {item.categoriaNombre ? <span className="pChip soft">{item.categoriaNombre}</span> : null}
        </div>

        <div className="pMediaBottom" aria-hidden="true">
          <div className="pMetaRow">
            <span className="pPrice">{money(Math.max(0, item.precioVenta))}</span>

            <div className="pMetaPill">
              <b>{item.talla ?? "\u2014"}</b>
            </div>

            <div className="pMetaPill">
              <span className="colorDot" style={{ background: item.colorHex ?? "transparent" }} />
            </div>
          </div>
        </div>
      </div>

      <div className="productBody pBody">
        <div className="pTitleRow">
          <div className="productName pName" title={item.nombreProducto}>
            {item.nombreProducto}
          </div>
        </div>

        <div className="pFooter">
          <span className="pCode" title={item.codigoBarras || ""}>
            {item.codigoBarras || ""}
          </span>
          <button type="button" className="pCta" onClick={onAdminProduct}>
            Administrar producto
          </button>
        </div>
      </div>
    </motion.article>
  );
}

export default function ProductosSection({
  sectionVariants,
  reduceMotion,
  feed,
  err,
  stageRef,
  stageMinH,
  switching,
  overlayOn,
  bootLoading,
  slowFetch,
  loadingMore,
  sentinelRef,
  onAdminProduct,
}: ProductosSectionProps) {
  const showSkeleton = (bootLoading && slowFetch) || (feed.items.length === 0 && switching && slowFetch);

  return (
    <motion.section className="homeCard" variants={sectionVariants}>
      <div className="sectionHead">
        <h2>Productos</h2>
        <div className="sectionActions">
          <div className="metaLine">
            <span>
              Mostrando <b>{feed.items.length}</b>{" "}
              <span className="mutedInline">
                / <b>{feed.total > 0 ? feed.total : "\u2014"}</b>
              </span>
            </span>
          </div>
        </div>
      </div>

      {err && <div className="mutedPad danger">{err}</div>}

      <div ref={stageRef} className="productStage" style={stageMinH ? { minHeight: stageMinH } : undefined}>
        {switching && overlayOn && <LoadingOverlay reduceMotion={reduceMotion} />}

        <AnimatePresence initial={false} mode="wait">
          <motion.div
            key={feed.key}
            className="gridWrap"
            initial={reduceMotion ? false : { opacity: 0, y: 8, filter: "blur(10px)" }}
            animate={reduceMotion ? undefined : { opacity: 1, y: 0, filter: "blur(0px)" }}
            exit={reduceMotion ? undefined : { opacity: 0, y: -6, filter: "blur(10px)" }}
            transition={reduceMotion ? undefined : { duration: 0.28, ease: easeOut }}
          >
            <div className={`productGrid ${switching ? "is-dim" : ""}`}>
              {showSkeleton ? (
                <ProductSkeleton />
              ) : feed.items.length === 0 ? (
                <motion.div
                  className="emptyState"
                  initial={reduceMotion ? false : { opacity: 0, y: 6, filter: "blur(8px)" }}
                  animate={reduceMotion ? undefined : { opacity: 1, y: 0, filter: "blur(0px)" }}
                  transition={reduceMotion ? undefined : { duration: 0.22, ease: easeOut }}
                >
                  No hay resultados con los filtros actuales.
                </motion.div>
              ) : (
                <AnimatePresence initial={false} mode={reduceMotion ? "sync" : "popLayout"}>
                  {feed.items.map((item) => (
                    <ProductCard key={item.varianteId} item={item} reduceMotion={reduceMotion} onAdminProduct={onAdminProduct} />
                  ))}
                </AnimatePresence>
              )}
            </div>

            <div className="feedFooter">
              {loadingMore && feed.items.length > 0 ? (
                <div className="feedLoading" aria-label={"Cargando m\u00e1s..."}>
                  <div className="dot" />
                  <div className="dot" />
                  <div className="dot" />
                </div>
              ) : null}

              {!feed.hasMore && feed.items.length > 0 ? <div className="feedEnd">Ya no hay m&aacute;s productos.</div> : null}
              <div ref={sentinelRef} style={{ height: 1 }} />
            </div>
          </motion.div>
        </AnimatePresence>
      </div>
    </motion.section>
  );
}
