import type { RefObject } from "react";
import { motion, type Variants } from "framer-motion";
import type { Categoria } from "../../../api/catalogo";
import { apiImgSrc } from "../catalogoUtils";
import { easeOut } from "../motion";
import { IconChevronLeft, IconChevronRight } from "./CatalogoIcons";

type CategoriasSectionProps = {
  sectionVariants: Variants;
  reduceMotion: boolean | null;
  cats: Categoria[];
  catsLoading: boolean;
  catsError: string | null;
  catId: number | null;
  switching: boolean;
  catRailRef: RefObject<HTMLDivElement | null>;
  catEndPadPx: number;
  onSelectCatId: (catId: number | null) => void;
  onScrollCats: (dir: -1 | 1) => void;
};

export default function CategoriasSection({
  sectionVariants,
  reduceMotion,
  cats,
  catsLoading,
  catsError,
  catId,
  switching,
  catRailRef,
  catEndPadPx,
  onSelectCatId,
  onScrollCats,
}: CategoriasSectionProps) {
  return (
    <motion.section className="homeCard" variants={sectionVariants}>
      <div className="sectionHead">
        <h2>Categor&iacute;as</h2>

        <div className="sectionActions">
          <button type="button" className={`pill ${catId === null ? "is-active" : ""}`} onClick={() => onSelectCatId(null)}>
            Todas
          </button>

          <div className="railNav">
            <button
              type="button"
              className="iconBtn"
              title="Anterior"
              onClick={() => onScrollCats(-1)}
              disabled={catsLoading || cats.length === 0}
            >
              <IconChevronLeft className="iconBtn__svg" />
            </button>
            <button
              type="button"
              className="iconBtn"
              title="Siguiente"
              onClick={() => onScrollCats(1)}
              disabled={catsLoading || cats.length === 0}
            >
              <IconChevronRight className="iconBtn__svg" />
            </button>
          </div>
        </div>
      </div>

      {catsLoading && <div className="mutedPad">Cargando categor&iacute;as...</div>}
      {catsError && <div className="mutedPad danger">{catsError}</div>}

      {!catsLoading && !catsError && cats.length > 0 && (
        <div className="catCarousel">
          <div ref={catRailRef} className="catRail" aria-label="Carrusel de categorias">
            {cats.map((c, i) => {
              const active = c.id === catId;
              const src = apiImgSrc(c.imagenUrl);

              return (
                <motion.button
                  key={c.id}
                  type="button"
                  className={`nfTile ${active ? "is-active" : ""} ${switching && active ? "is-pending" : ""}`}
                  disabled={switching && active}
                  aria-busy={switching && active}
                  onClick={() => onSelectCatId(active ? null : c.id)}
                  initial={reduceMotion ? false : { opacity: 0, y: 8, filter: "blur(8px)" }}
                  animate={reduceMotion ? undefined : { opacity: 1, y: 0, filter: "blur(0px)" }}
                  transition={reduceMotion ? undefined : { duration: 0.38, ease: easeOut, delay: Math.min(0.18, i * 0.02) }}
                  whileTap={reduceMotion ? undefined : { scale: 0.985 }}
                >
                  <div className="nfMedia">
                    {src ? (
                      <img className="nfImg" src={src} alt={c.nombre} loading="lazy" decoding="async" />
                    ) : (
                      <div className="nfFallback" aria-hidden="true">
                        {c.nombre?.slice(0, 1)?.toUpperCase() || "C"}
                      </div>
                    )}

                    <div className="nfOverlay" aria-hidden="true">
                      <div className="nfOverlay__top">
                        <div className="nfOverTitle">{c.nombre}</div>
                        <div className="nfOverSub">{active ? "Quitar filtro" : "Explorar productos"}</div>
                      </div>
                      <div className="nfOverlay__bottom">
                        <span className="nfCta">
                          {active ? "Quitar filtro" : "Ver"}
                          <span className="nfCta__spin" aria-hidden="true" />
                        </span>
                      </div>
                    </div>
                  </div>
                </motion.button>
              );
            })}

            <div aria-hidden="true" style={{ flex: `0 0 ${catEndPadPx}px` }} />
          </div>
        </div>
      )}
    </motion.section>
  );
}
