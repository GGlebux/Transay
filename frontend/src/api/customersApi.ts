import { http } from "./http";
import type { Customer, CustomerRole } from "./types";

export const customersApi = {
  // Текущие данные аутентификации
  async getMe(): Promise<Customer> {
    const { data } = await http.get<Customer>("/customers");
    return data;
  },

  // Список всех пользователей (ADMIN). На бэке это POST /customers/all
  async getAll(): Promise<Customer[]> {
    const { data } = await http.post<Customer[]>("/customers/all", {});
    return data;
  },

  // Сменить роль пользователя (ADMIN)
  async upgrade(id: number, role: CustomerRole): Promise<{ detail: string }> {
    const { data } = await http.post<{ detail: string }>("/customers/upgrade", { id, role });
    return data;
  },
};
