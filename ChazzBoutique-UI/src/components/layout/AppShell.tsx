import type { ReactNode } from "react";
import Sidebar, { type MenuKey } from "../Sidebar";

type AppShellProps = {
  active: MenuKey;
  onChange: (key: MenuKey) => void;
  onLogout: () => void;
  children: ReactNode;
};

export default function AppShell({ active, onChange, onLogout, children }: AppShellProps) {
  return (
    <div className="app-shell">
      <Sidebar active={active} onChange={onChange} onLogout={onLogout} />
      <main className="app-content">{children}</main>
    </div>
  );
}
