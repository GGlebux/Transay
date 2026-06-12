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
};
