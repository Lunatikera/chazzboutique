export const easeOut = [0.16, 1, 0.3, 1] as const;

export const fadeUp = {
  hidden: { opacity: 0, y: 10, filter: "blur(6px)" },
  show: { opacity: 1, y: 0, filter: "blur(0px)", transition: { duration: 0.55, ease: easeOut } },
};

export const pop = {
  hidden: { opacity: 0, scale: 0.98, y: 10, filter: "blur(8px)" },
  show: { opacity: 1, scale: 1, y: 0, filter: "blur(0px)", transition: { duration: 0.22, ease: easeOut } },
  exit: { opacity: 0, scale: 0.985, y: 8, filter: "blur(8px)", transition: { duration: 0.16, ease: easeOut } },
};
