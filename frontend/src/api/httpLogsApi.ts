import { http } from "./http";
import type { HttpLogDetail, HttpLogSummary, Page } from "./types";

export const httpLogsApi = {
  async list(page: number, size: number, onlyErrors: boolean): Promise<Page<HttpLogSummary>> {
    const { data } = await http.get<Page<HttpLogSummary>>("/admin/http-logs", {
      params: { page, size, onlyErrors },
    });
    return data;
  },

  async get(id: number): Promise<HttpLogDetail> {
    const { data } = await http.get<HttpLogDetail>(`/admin/http-logs/${id}`);
    return data;
  },

  async clear(): Promise<void> {
    await http.delete("/admin/http-logs");
  },
};
