import { AnimatePresence, motion } from "framer-motion";

export type ToastState<TType extends string = string> = {
  type: TType;
  text: string;
} | null;

type MotionToastProps<TType extends string = string> = {
  toast: ToastState<TType>;
  baseClassName: string;
  reduceMotion?: boolean | null;
  duration?: number;
};

export default function MotionToast<TType extends string = string>({
  toast,
  baseClassName,
  reduceMotion = false,
  duration = 0.18,
}: MotionToastProps<TType>) {
  return (
    <AnimatePresence>
      {toast && (
        <motion.div
          className={`${baseClassName} ${toast.type}`}
          initial={reduceMotion ? false : { opacity: 0, y: 12, scale: 0.98, filter: "blur(10px)" }}
          animate={reduceMotion ? undefined : { opacity: 1, y: 0, scale: 1, filter: "blur(0px)" }}
          exit={reduceMotion ? undefined : { opacity: 0, y: 10, scale: 0.985, filter: "blur(10px)" }}
          transition={reduceMotion ? undefined : { duration, ease: [0.16, 1, 0.3, 1] }}
        >
          {toast.text}
        </motion.div>
      )}
    </AnimatePresence>
  );
}
