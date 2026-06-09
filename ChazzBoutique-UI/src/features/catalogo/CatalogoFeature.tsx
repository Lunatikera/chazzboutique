import { useMemo } from "react";
import { motion, useReducedMotion } from "framer-motion";
import "../../styles/home.css";
import MotionToast from "../../components/ui/MotionToast";
import CatalogoHeader from "./components/CatalogoHeader";
import CategoriasSection from "./components/CategoriasSection";
import ProductosSection from "./components/ProductosSection";
import { makePageVariants, makeSectionVariants } from "./motion";
import { useCatalogo } from "./useCatalogo";

export default function CatalogoFeature() {
  const reduceMotion = useReducedMotion();
  const { state, refs, actions } = useCatalogo(reduceMotion);
  const pageVariants = useMemo(() => makePageVariants(reduceMotion), [reduceMotion]);
  const sectionVariants = useMemo(() => makeSectionVariants(reduceMotion), [reduceMotion]);

  return (
    <motion.div className="home" variants={pageVariants} initial="hidden" animate="show">
      <CatalogoHeader
        sectionVariants={sectionVariants}
        reduceMotion={reduceMotion}
        searchInputRef={refs.searchInputRef}
        filtro={state.filtro}
        catsCount={state.cats.length}
        itemsCount={state.feed.items.length}
        uiCatName={state.uiCatName}
        onFiltroChange={actions.setFiltro}
        onClearFiltro={() => actions.setFiltro("")}
      />

      <CategoriasSection
        sectionVariants={sectionVariants}
        reduceMotion={reduceMotion}
        cats={state.cats}
        catsLoading={state.catsLoading}
        catsError={state.catsError}
        catId={state.catId}
        switching={state.switching}
        catRailRef={refs.catRailRef}
        catEndPadPx={state.catEndPadPx}
        onSelectCatId={actions.setCatId}
        onScrollCats={actions.scrollCatsByOne}
      />

      <ProductosSection
        sectionVariants={sectionVariants}
        reduceMotion={reduceMotion}
        feed={state.feed}
        err={state.err}
        stageRef={refs.stageRef}
        stageMinH={state.stageMinH}
        switching={state.switching}
        overlayOn={state.overlayOn}
        bootLoading={state.bootLoading}
        slowFetch={state.slowFetch}
        loadingMore={state.loadingMore}
        sentinelRef={refs.sentinelRef}
        onAdminProduct={() => actions.showToast("ok", "Abrir administrador (pendiente)")}
      />

      <MotionToast toast={state.toast} baseClassName="homeToast" reduceMotion={reduceMotion} duration={0.22} />
    </motion.div>
  );
}
