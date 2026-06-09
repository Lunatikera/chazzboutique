import { useMemo, useState } from "react";
import AppShell from "./components/layout/AppShell";
import { useDisableEffectsWhileResizing } from "./components/layout/useDisableEffectsWhileResizing";
import type { MenuKey } from "./components/Sidebar";
import VentaPage from "./pages/VentaPage";
import HomePage from "./pages/HomePage";

export default function App() {
  useDisableEffectsWhileResizing();

  const [active, setActive] = useState<MenuKey>("venta");

  const content = useMemo(() => {
    if (active === "home") return <HomePage />;
    return <VentaPage />;
  }, [active]);

  return <AppShell active={active} onChange={setActive}>{content}</AppShell>;
}
