import type { AxiosError } from "axios";

// Форма ответа об ошибке от бэкенда: Spring ProblemDetail (RFC 7807) +
// поле errors (карта поле→сообщение) для ошибок валидации.
type ApiProblem = {
  title?: string;
  detail?: string;
  message?: string;
  status?: number;
  errors?: Record<string, string>;
};

// Достаёт человекочитаемое сообщение из ошибки axios/бэкенда.
// Приоритет: ошибки валидации (errors) → detail → message → title → сетевое сообщение → запасной текст.
export function getApiErrorMessage(
  err: unknown,
  fallback = "Произошла ошибка. Попробуйте позже."
): string {
  const data = (err as AxiosError<ApiProblem | string>)?.response?.data;

  if (typeof data === "string" && data.trim()) return data.trim();

  if (data && typeof data === "object") {
    const p = data as ApiProblem;
    if (p.errors && typeof p.errors === "object") {
      const msgs = Object.values(p.errors).filter((m): m is string => Boolean(m));
      if (msgs.length) return msgs.join("\n");
    }
    if (p.detail) return p.detail;
    if (p.message) return p.message;
    if (p.title) return p.title;
  }

  const axiosMsg = (err as AxiosError)?.message;
  if (axiosMsg) return axiosMsg;

  return fallback;
}
