import { http } from "./http";
import type { Indicator, IndicatorPayload } from "./types";

export const indicatorsApi = {
  async getAll(): Promise<Indicator[]> {
    const { data } = await http.get<Indicator[]>("/indicators");
    return data ?? [];
  },

  async create(payload: IndicatorPayload): Promise<Indicator> {
    const { data } = await http.post<Indicator>("/indicators", payload);
    return data;
  },

  // У индикаторов обновление — PUT /{id}
  async update(id: number, payload: IndicatorPayload): Promise<Indicator> {
    const { data } = await http.put<Indicator>(`/indicators/${id}`, payload);
    return data;
  },

  async remove(id: number): Promise<void> {
    await http.delete(`/indicators/${id}`);
  },
};
