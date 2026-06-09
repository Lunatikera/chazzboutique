import { useEffect } from "react";

export function useDisableEffectsWhileResizing() {
  useEffect(() => {
    let t: number | null = null;

    const onResize = () => {
      document.documentElement.classList.add("is-resizing");
      if (t) window.clearTimeout(t);
      t = window.setTimeout(() => {
        document.documentElement.classList.remove("is-resizing");
        t = null;
      }, 160);
    };

    window.addEventListener("resize", onResize, { passive: true });
    return () => {
      window.removeEventListener("resize", onResize);
      if (t) window.clearTimeout(t);
      document.documentElement.classList.remove("is-resizing");
    };
  }, []);
}
