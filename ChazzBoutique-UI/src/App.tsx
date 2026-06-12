import { useMemo, useState } from "react";
import AppShell from "./components/layout/AppShell";
import { useDisableEffectsWhileResizing } from "./components/layout/useDisableEffectsWhileResizing";
import type { MenuKey } from "./components/Sidebar";
import { useAuth } from "./auth/AuthContext";
import VentaPage from "./pages/VentaPage";
import HomePage from "./pages/HomePage";
import ProductosPage from "./pages/ProductosPage";
import CategoriasPage from "./pages/CategoriasPage";
import ReportesPage from "./pages/ReportesPage";
import ProveedoresPage from "./pages/ProveedoresPage";
import LoginPage from "./pages/LoginPage";

export default function App() {
  useDisableEffectsWhileResizing();

  const { user, logout } = useAuth();
  const [active, setActive] = useState<MenuKey>("venta");

  const content = useMemo(() => {
    switch (active) {
      case "home":
        return <HomePage />;
      case "productos":
        return <ProductosPage />;
      case "categorias":
        return <CategoriasPage />;
      case "reportes":
        return <ReportesPage />;
      case "proveedores":
        return <ProveedoresPage />;
      case "venta":
      default:
        return <VentaPage />;
    }
  }, [active]);

  if (!user) return <LoginPage />;

  return (
    <AppShell active={active} onChange={setActive} onLogout={logout}>
      {content}
    </AppShell>
  );
}
