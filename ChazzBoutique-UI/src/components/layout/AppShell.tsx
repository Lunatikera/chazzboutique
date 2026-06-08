import type { ReactNode } from "react";
import Sidebar, { type MenuKey } from "../Sidebar";

type AppShellProps = {
  active: MenuKey;
  onChange: (key: MenuKey) => void;
  children: ReactNode;
};

export default function AppShell({ active, onChange, children }: AppShellProps) {
  return (
    <div className="app-shell">
      <Sidebar active={active} onChange={onChange} />
      <main className="app-content">{children}</main>
    </div>
  );
}
