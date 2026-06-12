// Бэк отдаёт даты в ISO (yyyy-MM-dd), а для записи (person, measure) ждёт dd.MM.yyyy.

// yyyy-MM-dd -> dd.MM.yyyy (для тела POST/PATCH)
export function isoToServer(value: string): string {
  if (!value || !value.includes("-")) return "";
  const [year, month, day] = value.split("-");
  if (!year || !month || !day) return "";
  return `${day}.${month}.${year}`;
}

// dd.MM.yyyy | yyyy-MM-dd -> yyyy-MM-dd (для <input type="date">)
export function toInputDate(value: string): string {
  if (!value) return "";
  if (value.includes("-")) return value;
  if (value.includes(".")) {
    const [day, month, year] = value.split(".");
    return `${year}-${month}-${day}`;
  }
  return "";
}

// Любая дата -> dd.MM.yy (компактный вывод в таблицах)
export function formatShort(value: string): string {
  if (!value) return "";
  const d = new Date(value);
  if (isNaN(d.getTime())) return value;
  const day = String(d.getDate()).padStart(2, "0");
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const year = String(d.getFullYear()).slice(-2);
  return `${day}.${month}.${year}`;
}

export const todayISO = (): string => new Date().toISOString().slice(0, 10);
