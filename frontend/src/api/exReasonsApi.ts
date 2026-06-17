import { http } from "./http";
import type { Reason } from "./types";

export const exReasonsApi = {
  // Список исключённых причин «своего» человека
  async getAll(): Promise<Reason[]> {
    const { data } = await http.get<Reason[]>("/people/ex_reasons");
    return data ?? [];
  },

  // Добавить исключённые причины (тело — массив id-шников)
  async add(reasonIds: number[]): Promise<Reason[]> {
    const { data } = await http.post<Reason[]>("/people/ex_reasons", reasonIds);
    return data ?? [];
  },

  // Убрать причину из исключённых
  async remove(reasonId: number): Promise<void> {
    await http.delete(`/people/ex_reasons/${reasonId}`);
  },

  /* ---------- То же для члена семьи (по personId) ---------- */

  async getAllFor(personId: number): Promise<Reason[]> {
    const { data } = await http.get<Reason[]>(`/people/family/${personId}/ex_reasons`);
    return data ?? [];
  },

  async addFor(personId: number, reasonIds: number[]): Promise<Reason[]> {
    const { data } = await http.post<Reason[]>(`/people/family/${personId}/ex_reasons`, reasonIds);
    return data ?? [];
  },

  async removeFor(personId: number, reasonId: number): Promise<void> {
    await http.delete(`/people/family/${personId}/ex_reasons/${reasonId}`);
  },
};
