import { http } from "./http";
import type { IndicatorGroup, IndicatorGroupPayload } from "./types";

export const groupsApi = {
  async getAll(): Promise<IndicatorGroup[]> {
    const { data } = await http.get<IndicatorGroup[]>("/groups");
    return data ?? [];
  },

  async create(payload: IndicatorGroupPayload): Promise<IndicatorGroup> {
    const { data } = await http.post<IndicatorGroup>("/groups", payload);
    return data;
  },

  // Группы обновляются через PATCH /{id}
  async update(id: number, payload: IndicatorGroupPayload): Promise<IndicatorGroup> {
    const { data } = await http.patch<IndicatorGroup>(`/groups/${id}`, payload);
    return data;
  },

  async remove(id: number): Promise<void> {
    await http.delete(`/groups/${id}`);
  },
};
