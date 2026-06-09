export const easeOut = [0.16, 1, 0.3, 1] as const;

export function makePageVariants(reduceMotion: boolean | null) {
  return {
    hidden: reduceMotion ? { opacity: 1 } : { opacity: 0, y: 10, filter: "blur(10px)" },
    show: reduceMotion
      ? { opacity: 1 }
      : {
          opacity: 1,
          y: 0,
          filter: "blur(0px)",
          transition: { duration: 0.62, ease: easeOut, when: "beforeChildren", staggerChildren: 0.06 },
        },
  };
}

export function makeSectionVariants(reduceMotion: boolean | null) {
  return {
    hidden: reduceMotion ? { opacity: 1 } : { opacity: 0, y: 12, filter: "blur(12px)" },
    show: reduceMotion
      ? { opacity: 1 }
      : { opacity: 1, y: 0, filter: "blur(0px)", transition: { duration: 0.52, ease: easeOut } },
  };
}
