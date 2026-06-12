import axios from "axios";
import type { AxiosError, AxiosInstance, InternalAxiosRequestConfig } from "axios";
import { tokenStorage } from "./tokenStorage";

const baseURL = import.meta.env.VITE_API_BASE_URL as string;

type RefreshResponse = { accessToken: string };

type RetriableConfig = InternalAxiosRequestConfig & { _retry?: boolean };

let isRefreshing = false;
let refreshQueue: Array<(token: string | null) => void> = [];

function resolveQueue(token: string | null) {
  refreshQueue.forEach((cb) => cb(token));
  refreshQueue = [];
}

export const http: AxiosInstance = axios.create({
  baseURL,
  withCredentials: true, // чтобы refresh cookie уходила/принималась
});

http.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const url = String(config.url || "");

  const isAuthCall =
    url.includes("/auth/login") ||
    url.includes("/auth/register") ||
    url.includes("/auth/refresh") ||
    url.includes("/auth/logout") ||
    url.includes("/auth/verification");

  if (!isAuthCall) {
    const token = tokenStorage.get();
    if (token) {
      config.headers = config.headers ?? {};
      config.headers.Authorization = `Bearer ${token}`;
    }
  }

  return config;
});


http.interceptors.response.use(
  (r) => r,
  async (error: AxiosError) => {
    const original = error.config as RetriableConfig | undefined;
    if (!original) throw error;

    const status = error.response?.status;
    const url = String(original.url || "");

    const isAuthCall =
      url.includes("/auth/login") ||
      url.includes("/auth/register") ||
      url.includes("/auth/refresh") ||
      url.includes("/auth/logout") ||
      url.includes("/auth/verification");

    if (status !== 401 || original._retry || isAuthCall) throw error;

    original._retry = true;

    if (isRefreshing) {
      return new Promise((resolve, reject) => {
        refreshQueue.push((token) => {
          if (!token) return reject(error);
          original.headers = original.headers ?? {};
          original.headers.Authorization = `Bearer ${token}`;
          resolve(http(original));
        });
      });
    }

    isRefreshing = true;

    try {
      // ВАЖНО: refresh через тот же инстанс, чтобы baseURL + credentials были одинаковые
      const { data } = await http.post<RefreshResponse>("/auth/refresh", {});
      const newToken = data.accessToken;

      tokenStorage.set(newToken);
      resolveQueue(newToken);

      original.headers = original.headers ?? {};
      original.headers.Authorization = `Bearer ${newToken}`;

      return http(original);
    } catch (e) {
      tokenStorage.clear();
      resolveQueue(null);
      throw e;
    } finally {
      isRefreshing = false;
    }
  }
);