import { useEffect, useMemo, useRef, useState } from "react";
import { motion } from "framer-motion";
import "./Sidebar.css";

export type MenuKey =
  | "home"
  | "venta"
  | "productos"
  | "categorias"
  | "reportes"
  | "proveedores";

type Props = {
  active: MenuKey;
  onChange: (k: MenuKey) => void;
  onLogout: () => void;
};

function useMediaQuery(query: string) {
  const get = () =>
    typeof window !== "undefined" &&
    typeof window.matchMedia !== "undefined" &&
    window.matchMedia(query).matches;

  const [matches, setMatches] = useState<boolean>(get);

  useEffect(() => {
    if (typeof window === "undefined" || typeof window.matchMedia === "undefined") return;
    const m = window.matchMedia(query);
    const onChange = () => setMatches(m.matches);

    onChange();
    if (m.addEventListener) m.addEventListener("change", onChange);
    else m.addListener(onChange);

    return () => {
      if (m.removeEventListener) m.removeEventListener("change", onChange);
      else m.removeListener(onChange);
    };
  }, [query]);

  return matches;
}

function Icon({ name, active }: { name: MenuKey; active: boolean }) {
  const common = {
    width: 18,
    height: 18,
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "currentColor",
    strokeWidth: 2,
    strokeLinecap: "round" as const,
    strokeLinejoin: "round" as const,
    className: `sb__icon ${active ? "is-active" : ""}`,
    "aria-hidden": true,
  };

  switch (name) {
    case "home":
      return (
        <svg {...common}>
          <rect x="3" y="3" width="8" height="8" rx="2" />
          <rect x="13" y="3" width="8" height="8" rx="2" />
          <rect x="3" y="13" width="8" height="8" rx="2" />
          <rect x="13" y="13" width="8" height="8" rx="2" />
        </svg>
      );
    case "venta":
      return (
        <svg {...common}>
          <circle cx="9" cy="20" r="1" />
          <circle cx="17" cy="20" r="1" />
          <path d="M3 4h2l2.4 12.2a2 2 0 0 0 2 1.6h7.9a2 2 0 0 0 2-1.6L22 8H6.2" />
        </svg>
      );
    case "productos":
      return (
        <svg {...common}>
          <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z" />
          <polyline points="3.27 6.96 12 12.01 20.73 6.96" />
          <line x1="12" y1="22.08" x2="12" y2="12" />
        </svg>
      );
    case "categorias":
      return (
        <svg {...common}>
          <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z" />
          <line x1="7" y1="7" x2="7.01" y2="7" />
        </svg>
      );
    case "reportes":
      return (
        <svg {...common}>
          <line x1="18" y1="20" x2="18" y2="10" />
          <line x1="12" y1="20" x2="12" y2="4" />
          <line x1="6" y1="20" x2="6" y2="14" />
        </svg>
      );
    case "proveedores":
      return (
        <svg {...common}>
          <rect x="1" y="3" width="15" height="13" />
          <polygon points="16 8 20 8 23 11 23 16 16 16 16 8" />
          <circle cx="5.5" cy="18.5" r="2.5" />
          <circle cx="18.5" cy="18.5" r="2.5" />
        </svg>
      );
    default:
      return null;
  }
}

function LogoutIcon() {
  return (
    <svg
      width={18}
      height={18}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth={2}
      strokeLinecap="round"
      strokeLinejoin="round"
      className="sb__icon"
      aria-hidden="true"
    >
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
      <polyline points="16 17 21 12 16 7" />
      <line x1="21" y1="12" x2="9" y2="12" />
    </svg>
  );
}

const items: { key: MenuKey; label: string }[] = [
  { key: "venta", label: "Venta" },
  { key: "home", label: "Catálogo" },
  { key: "productos", label: "Productos" },
  { key: "categorias", label: "Categorías" },
  { key: "reportes", label: "Reportes" },
  { key: "proveedores", label: "Proveedores" },
];

export default function Sidebar({ active, onChange, onLogout }: Props) {
  const isMobile = useMediaQuery("(max-width: 900px)");

  // Desktop hover open
  const [hoverOpen, setHoverOpen] = useState(false);

  const dims = useMemo(() => ({ collapsed: 92, expanded: 256 }), []);
  const openDelayMs = 120;
  const closeDelayMs = 90;

  const tOpen = useRef<number | null>(null);
  const tClose = useRef<number | null>(null);

  function clearTimers() {
    if (tOpen.current) window.clearTimeout(tOpen.current);
    if (tClose.current) window.clearTimeout(tClose.current);
    tOpen.current = null;
    tClose.current = null;
  }

  function scheduleOpen() {
    if (isMobile) return;
    if (hoverOpen) return;
    if (tOpen.current) return;
    if (tClose.current) {
      window.clearTimeout(tClose.current);
      tClose.current = null;
    }
    tOpen.current = window.setTimeout(() => {
      tOpen.current = null;
      setHoverOpen(true);
    }, openDelayMs);
  }

  function scheduleClose() {
    if (isMobile) return;
    if (!hoverOpen) {
      if (tOpen.current) {
        window.clearTimeout(tOpen.current);
        tOpen.current = null;
      }
      return;
    }
    if (tClose.current) return;
    if (tOpen.current) {
      window.clearTimeout(tOpen.current);
      tOpen.current = null;
    }
    tClose.current = window.setTimeout(() => {
      tClose.current = null;
      setHoverOpen(false);
    }, closeDelayMs);
  }

  const expanded = !isMobile && hoverOpen;

  return (
    <>
      {/* Hover zone solo desktop */}
      {!isMobile && (
        <div
          className="sb-zone"
          onMouseEnter={scheduleOpen}
          onMouseLeave={scheduleClose}
          aria-hidden="true"
        />
      )}

      <motion.aside
        className={`sb ${expanded ? "is-open" : "is-closed"} ${isMobile ? "is-mobile" : ""}`}
        onMouseEnter={scheduleOpen}
        onMouseLeave={scheduleClose}
        onFocusCapture={() => {
          if (isMobile) return;
          clearTimers();
          setHoverOpen(true);
        }}
        onBlurCapture={(e) => {
          if (isMobile) return;
          const next = e.relatedTarget as Node | null;
          if (!next || !e.currentTarget.contains(next)) {
            clearTimers();
            setHoverOpen(false);
          }
        }}
        animate={isMobile ? undefined : { width: expanded ? dims.expanded : dims.collapsed }}
        transition={{ type: "spring", stiffness: 240, damping: 30, mass: 0.9 }}
        style={isMobile ? undefined : { width: dims.collapsed }}
        aria-label={isMobile ? "Barra inferior" : "Sidebar"}
      >
        {/* Brand solo desktop */}
        {!isMobile && (
          <div className="sb__brand">
            <img
              className="sb__brandLogo"
              src="/images/chazzLogoBlack.png"
              alt="Chazz Boutique"
              draggable={false}
            />
          </div>
        )}

        <nav className="sb__nav" aria-label="Menú">
          {items.map((it) => {
            const isActive = active === it.key;

            return (
              <button
                key={it.key}
                type="button"
                title={it.label}
                onClick={() => onChange(it.key)}
                className={`sb__item ${isActive ? "is-active" : ""}`}
              >
                <span className="sb__iconWrap" aria-hidden="true">
                  <Icon name={it.key} active={isActive} />
                </span>

                {/* Labels solo desktop cuando expanded */}
                {!isMobile && (
                  <span className={`sb__labelSlot ${expanded ? "is-open" : "is-closed"}`}>
                    <span className="sb__label">{it.label}</span>
                  </span>
                )}

                {/* Rail solo desktop */}
                {!isMobile && expanded && isActive && <span className="sb__activeRail" />}
              </button>
            );
          })}

          <button
            type="button"
            title="Cerrar sesión"
            onClick={onLogout}
            className="sb__item sb__logout"
          >
            <span className="sb__iconWrap" aria-hidden="true">
              <LogoutIcon />
            </span>

            {!isMobile && (
              <span className={`sb__labelSlot ${expanded ? "is-open" : "is-closed"}`}>
                <span className="sb__label">Cerrar sesión</span>
              </span>
            )}
          </button>
        </nav>
      </motion.aside>
    </>
  );
}
