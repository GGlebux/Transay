import { http } from "./http";
import type { Person, PersonPayload } from "./types";

export type { Person, PersonPayload } from "./types";

export const peopleApi = {
  // Данные «своего» человека (текущего пользователя)
  async getMe(): Promise<Person> {
    const { data } = await http.get<Person>("/people");
    return data;
  },

  // Создать «своего» человека
  async create(payload: PersonPayload): Promise<Person> {
    const { data } = await http.post<Person>("/people", payload);
    return data;
  },

  // Обновить «своего» человека
  async update(payload: PersonPayload): Promise<Person> {
    const { data } = await http.patch<Person>("/people", payload);
    return data;
  },

  // Удалить «своего» человека
  async remove(): Promise<void> {
    await http.delete("/people");
  },

  // Список всех людей (ADMIN/EDITOR) — только чтение
  async getAll(): Promise<Person[]> {
    const { data } = await http.get<Person[]>("/people/all");
    return data;
  },

  /* ---------- Семья: люди, привязанные к аккаунту ---------- */

  async getFamily(): Promise<Person[]> {
    const { data } = await http.get<Person[]>("/people/family");
    return Array.isArray(data) ? data : [];
  },

  async getFamilyMember(id: number): Promise<Person> {
    const { data } = await http.get<Person>(`/people/family/${id}`);
    return data;
  },

  async createFamily(payload: PersonPayload): Promise<Person> {
    const { data } = await http.post<Person>("/people/family", payload);
    return data;
  },

  async updateFamily(id: number, payload: PersonPayload): Promise<Person> {
    const { data } = await http.patch<Person>(`/people/family/${id}`, payload);
    return data;
  },

  async removeFamily(id: number): Promise<void> {
    await http.delete(`/people/family/${id}`);
  },
};
