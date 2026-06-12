import { http } from "./http";
import type { Transcript, TranscriptPayload } from "./types";

export const transcriptsApi = {
  async getAll(): Promise<Transcript[]> {
    const { data } = await http.get<Transcript[]>("/transcripts");
    return data ?? [];
  },

  async create(payload: TranscriptPayload): Promise<Transcript> {
    const { data } = await http.post<Transcript>("/transcripts", payload);
    return data;
  },

  // Транскрипции обновляются через PUT /{id}
  async update(id: number, payload: TranscriptPayload): Promise<Transcript> {
    const { data } = await http.put<Transcript>(`/transcripts/${id}`, payload);
    return data;
  },

  async remove(id: number): Promise<void> {
    await http.delete(`/transcripts/${id}`);
  },
};
