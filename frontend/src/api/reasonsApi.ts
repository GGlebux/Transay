import { http } from "./http";
import type { Reason } from "./types";

// Бэк принимает СЫРУЮ строку (@RequestBody String) — шлём text/plain без JSON-обёртки
const plainText = {
  headers: { "Content-Type": "text/plain; charset=utf-8" },
  transformRequest: [(d: any) => d],
};

export const reasonsApi = {
  async getAll(): Promise<Reason[]> {
    const { data } = await http.get<Reason[]>("/reasons");
    return data ?? [];
  },

  async create(name: string): Promise<Reason> {
    const { data } = await http.post<Reason>("/reasons", name.trim(), plainText);
    return data;
  },

  async update(id: number, name: string): Promise<Reason> {
    const { data } = await http.patch<Reason>(`/reasons/${id}`, name.trim(), plainText);
    return data;
  },

  async remove(id: number): Promise<void> {
    await http.delete(`/reasons/${id}`);
  },
};
