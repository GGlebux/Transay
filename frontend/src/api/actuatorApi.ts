import { http } from "./http";
import type { Health, LoggersResponse } from "./types";

// Actuator живёт в корне сайта (/actuator), а не под /api — переопределяем baseURL.
const ROOT = { baseURL: "" };

export const actuatorApi = {
  async health(): Promise<Health> {
    const { data } = await http.get<Health>("/actuator/health", ROOT);
    return data;
  },

  async metric(name: string): Promise<number | null> {
    try {
      const { data } = await http.get(`/actuator/metrics/${name}`, ROOT);
      return data?.measurements?.[0]?.value ?? null;
    } catch {
      return null;
    }
  },

  async loggers(): Promise<LoggersResponse> {
    const { data } = await http.get<LoggersResponse>("/actuator/loggers", ROOT);
    return data;
  },

  async setLevel(name: string, level: string | null): Promise<void> {
    await http.post(`/actuator/loggers/${name}`, { configuredLevel: level }, ROOT);
  },

  async logfile(): Promise<string> {
    const { data } = await http.get("/actuator/logfile", {
      ...ROOT,
      responseType: "text",
      transformResponse: (d) => d,
    });
    return data as string;
  },
};
