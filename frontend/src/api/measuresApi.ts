import { http } from "./http";
import type { SummaryGroup, DecryptResponse, MeasurePayload, Measure } from "./types";

export const measuresApi = {
  // Сводная таблица «своих» измерений
  async getSummary(): Promise<SummaryGroup[]> {
    const { data } = await http.get<SummaryGroup[]>("/people/measures");
    return Array.isArray(data) ? data : [];
  },

  // Расшифровка по дате. ВАЖНО: date в ISO (yyyy-MM-dd)
  async decrypt(dateISO: string): Promise<DecryptResponse> {
    const { data } = await http.get<DecryptResponse>("/people/measures/decrypt", {
      params: { date: dateISO },
    });
    return data ?? {};
  },

  // Создать измерение (regDate уже в dd.MM.yyyy)
  async create(payload: MeasurePayload): Promise<Measure> {
    const { data } = await http.post<Measure>("/people/measures", payload);
    return data;
  },

  // Обновить измерение по id
  async update(measureId: number, payload: MeasurePayload): Promise<Measure> {
    const { data } = await http.patch<Measure>(`/people/measures/${measureId}`, payload);
    return data;
  },

  async remove(measureId: number): Promise<void> {
    await http.delete(`/people/measures/${measureId}`);
  },
};
