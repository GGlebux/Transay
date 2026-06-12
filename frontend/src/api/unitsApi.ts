import { http } from "./http";
import type { Unit } from "./types";

// Бэк принимает СЫРУЮ строку (@RequestBody String) — шлём text/plain
const plainText = {
  headers: { "Content-Type": "text/plain; charset=utf-8" },
  transformRequest: [(d: any) => d],
};

export const unitsApi = {
  async getAll(): Promise<Unit[]> {
    const { data } = await http.get<Unit[]>("/indicators/units");
    return data ?? [];
  },

  async create(name: string): Promise<Unit> {
    const { data } = await http.post<Unit>("/indicators/units", name.trim(), plainText);
    return data;
  },

  async update(id: number, name: string): Promise<Unit> {
    const { data } = await http.patch<Unit>(`/indicators/units/${id}`, name.trim(), plainText);
    return data;
  },

  async remove(id: number): Promise<void> {
    await http.delete(`/indicators/units/${id}`);
  },
};
