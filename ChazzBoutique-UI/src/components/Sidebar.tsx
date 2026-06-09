import { useEffect, useMemo, useRef, useState } from "react";
import { motion } from "framer-motion";
import "./Sidebar.css";

export type MenuKey = "home" | "venta";

type Props = {
  active: MenuKey;
  onChange: (k: MenuKey) => void;
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
    default:
      return null;
  }
}

const items: { key: MenuKey; label: string }[] = [
  { key: "venta", label: "Venta" },
  { key: "home", label: "Catálogo" },
];

export default function Sidebar({ active, onChange }: Props) {
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
        </nav>
      </motion.aside>
    </>
  );
}
