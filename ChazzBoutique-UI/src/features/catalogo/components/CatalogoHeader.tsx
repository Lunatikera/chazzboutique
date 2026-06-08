import type { RefObject } from "react";
import { AnimatePresence, motion, type Variants } from "framer-motion";
import AnimatedKpiNumber from "./AnimatedKpiNumber";
import { IconBox, IconGrid, IconSearch, IconX } from "./CatalogoIcons";
import { easeOut } from "../motion";

type CatalogoHeaderProps = {
  sectionVariants: Variants;
  reduceMotion: boolean | null;
  searchInputRef: RefObject<HTMLInputElement | null>;
  filtro: string;
  catsCount: number;
  itemsCount: number;
  uiCatName: string;
  onFiltroChange: (value: string) => void;
  onClearFiltro: () => void;
};

export default function CatalogoHeader({
  sectionVariants,
  reduceMotion,
  searchInputRef,
  filtro,
  catsCount,
  itemsCount,
  uiCatName,
  onFiltroChange,
  onClearFiltro,
}: CatalogoHeaderProps) {
  return (
    <motion.div className="homeHead" variants={sectionVariants}>
      <div className="homeTitle">
        <h1>Cat&aacute;logo</h1>
        <p>Explora categor&iacute;as y productos con UI moderna.</p>
      </div>

      <div className="homeSearch">
        <IconSearch className="homeSearch__icon" />
        <input
          ref={searchInputRef}
          value={filtro}
          onChange={(e) => onFiltroChange(e.target.value)}
          placeholder="Buscar por nombre..."
          spellCheck={false}
          aria-label="Buscar productos"
        />
        {filtro.trim() !== "" && (
          <button className="iconBtn" type="button" title="Limpiar" onClick={onClearFiltro}>
            <IconX className="iconBtn__svg" />
          </button>
        )}
      </div>

      <motion.div
        className="homeKpis"
        layout={!reduceMotion}
        transition={reduceMotion ? undefined : { type: "spring", stiffness: 520, damping: 42, mass: 0.85 }}
      >
        <motion.div className="chip" layout="position">
          <IconGrid className="chip__icon" />
          <span>Categor&iacute;as</span>
          <b className="chipNum">
            <AnimatedKpiNumber value={catsCount} reduceMotion={reduceMotion} />
          </b>
        </motion.div>

        <motion.div className="chip" layout="position">
          <IconBox className="chip__icon" />
          <span>Mostrando</span>
          <b className="chipNum">
            <AnimatePresence initial={false} mode="popLayout">
              <motion.span
                key={itemsCount}
                className="chipNum__val"
                initial={reduceMotion ? false : { opacity: 0, y: 5, filter: "blur(8px)" }}
                animate={reduceMotion ? undefined : { opacity: 1, y: 0, filter: "blur(0px)" }}
                exit={reduceMotion ? undefined : { opacity: 0, y: -5, filter: "blur(8px)" }}
                transition={reduceMotion ? undefined : { duration: 0.22, ease: easeOut }}
              >
                {itemsCount}
              </motion.span>
            </AnimatePresence>
          </b>
        </motion.div>

        <motion.div className="chip chip--soft chip--filter" layout="position">
          <span>Filtro</span>

          <b className="chipValue" title={uiCatName}>
            <AnimatePresence initial={false} mode="popLayout">
              <motion.span
                key={uiCatName}
                className="chipValue__text"
                initial={reduceMotion ? false : { opacity: 0, y: 5, filter: "blur(8px)" }}
                animate={reduceMotion ? undefined : { opacity: 1, y: 0, filter: "blur(0px)" }}
                exit={reduceMotion ? undefined : { opacity: 0, y: -5, filter: "blur(8px)" }}
                transition={reduceMotion ? undefined : { duration: 0.22, ease: easeOut }}
              >
                {uiCatName}
              </motion.span>
            </AnimatePresence>
          </b>
        </motion.div>
      </motion.div>
    </motion.div>
  );
}
