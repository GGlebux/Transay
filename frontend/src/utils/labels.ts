const RU: Record<string, string> = {
  MALE: "Мужской",
  FEMALE: "Женский",
  BOTH: "Обоеполый",
  BASE: "Базовое",
  GRAVID: "Беременность",
  MENSES: "Менструация",
  OK: "Норма",
  RAISE: "Повышено",
  FALL: "Понижено",
  USER: "Пользователь",
  EDITOR: "Редактор",
  ADMIN: "Администратор",
  PENDING: "Ожидает",
  ACTIVE: "Активен",
};

export function enumLabel(code: string | null | undefined): string {
  if (!code) return "—";
  if (RU[code]) return RU[code];
  return code.charAt(0).toUpperCase() + code.slice(1).toLowerCase();
}
