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

  /* ---------- Те же операции для члена семьи (по personId) ---------- */

  async getSummaryFor(personId: number): Promise<SummaryGroup[]> {
    const { data } = await http.get<SummaryGroup[]>(`/people/family/${personId}/measures`);
    return Array.isArray(data) ? data : [];
  },

  async decryptFor(personId: number, dateISO: string): Promise<DecryptResponse> {
    const { data } = await http.get<DecryptResponse>(`/people/family/${personId}/measures/decrypt`, {
      params: { date: dateISO },
    });
    return data ?? {};
  },

  async createFor(personId: number, payload: MeasurePayload): Promise<Measure> {
    const { data } = await http.post<Measure>(`/people/family/${personId}/measures`, payload);
    return data;
  },

  async updateFor(personId: number, measureId: number, payload: MeasurePayload): Promise<Measure> {
    const { data } = await http.patch<Measure>(`/people/family/${personId}/measures/${measureId}`, payload);
    return data;
  },

  async removeFor(personId: number, measureId: number): Promise<void> {
    await http.delete(`/people/family/${personId}/measures/${measureId}`);
  },
};
