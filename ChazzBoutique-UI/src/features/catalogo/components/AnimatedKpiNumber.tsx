import { AnimatePresence, motion } from "framer-motion";
import { easeOut } from "../motion";

type AnimatedKpiNumberProps = {
  value: number;
  reduceMotion: boolean | null;
};

export default function AnimatedKpiNumber({ value, reduceMotion }: AnimatedKpiNumberProps) {
  if (reduceMotion) return <span className="chipNum__val">{value}</span>;

  return (
    <AnimatePresence initial={false} mode="popLayout">
      <motion.span
        key={value}
        className="chipNum__val"
        initial={{ opacity: 0, y: 6 }}
        animate={{ opacity: 1, y: 0 }}
        exit={{ opacity: 0, y: -6 }}
        transition={{ duration: 0.22, ease: easeOut }}
      >
        {value}
      </motion.span>
    </AnimatePresence>
  );
}
