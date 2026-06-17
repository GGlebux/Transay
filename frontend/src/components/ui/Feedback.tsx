import { createContext, useCallback, useContext, useRef, useState } from "react";
import type { ReactNode } from "react";
import "../../styles/feedback.css";

type ToastType = "error" | "success" | "info";
type Toast = { id: number; message: string; type: ToastType };

type ConfirmOptions = {
  title?: string;
  okText?: string;
  cancelText?: string;
  danger?: boolean;
};
type ConfirmState =
  | (ConfirmOptions & { message: string; resolve: (v: boolean) => void })
  | null;

type FeedbackCtx = {
  notify: (message: string, type?: ToastType) => void;
  confirm: (message: string, options?: ConfirmOptions) => Promise<boolean>;
};

const FeedbackContext = createContext<FeedbackCtx | null>(null);

const ICONS: Record<ToastType, string> = { error: "!", success: "✓", info: "i" };

export function FeedbackProvider({ children }: { children: ReactNode }) {
  const [toasts, setToasts] = useState<Toast[]>([]);
  const [confirmState, setConfirmState] = useState<ConfirmState>(null);
  const idRef = useRef(0);

  const dismiss = useCallback((id: number) => {
    setToasts((list) => list.filter((t) => t.id !== id));
  }, []);

  const notify = useCallback(
    (message: string, type: ToastType = "info") => {
      const id = ++idRef.current;
      setToasts((list) => [...list, { id, message, type }]);
      window.setTimeout(() => dismiss(id), 4500);
    },
    [dismiss]
  );

  const confirm = useCallback(
    (message: string, options?: ConfirmOptions) =>
      new Promise<boolean>((resolve) => {
        setConfirmState({ message, resolve, ...options });
      }),
    []
  );

  const closeConfirm = (value: boolean) => {
    confirmState?.resolve(value);
    setConfirmState(null);
  };

  return (
    <FeedbackContext.Provider value={{ notify, confirm }}>
      {children}

      <div className="toast-stack">
        {toasts.map((t) => (
          <div
            key={t.id}
            className={`toast toast--${t.type}`}
            onClick={() => dismiss(t.id)}
            role="alert"
          >
            <span className="toast__icon" aria-hidden="true">
              {ICONS[t.type]}
            </span>
            <div className="toast__body">{t.message}</div>
          </div>
        ))}
      </div>

      {confirmState && (
        <div className="confirm-backdrop" onClick={() => closeConfirm(false)}>
          <div
            className="confirm-card"
            onClick={(e) => e.stopPropagation()}
            role="dialog"
            aria-modal="true"
          >
            <h4>{confirmState.title ?? "Подтвердите действие"}</h4>
            <p>{confirmState.message}</p>
            <div className="confirm-actions">
              <button
                className="confirm-btn confirm-btn--ghost"
                onClick={() => closeConfirm(false)}
              >
                {confirmState.cancelText ?? "Отмена"}
              </button>
              <button
                className={`confirm-btn ${confirmState.danger ? "confirm-btn--danger" : "confirm-btn--primary"}`}
                onClick={() => closeConfirm(true)}
              >
                {confirmState.okText ?? "Подтвердить"}
              </button>
            </div>
          </div>
        </div>
      )}
    </FeedbackContext.Provider>
  );
}

export function useToast() {
  const ctx = useContext(FeedbackContext);
  if (!ctx) throw new Error("useToast must be used inside FeedbackProvider");
  return ctx.notify;
}

export function useConfirm() {
  const ctx = useContext(FeedbackContext);
  if (!ctx) throw new Error("useConfirm must be used inside FeedbackProvider");
  return ctx.confirm;
}
