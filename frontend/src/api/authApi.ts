import { http } from "./http";
import { tokenStorage } from "./tokenStorage";

type LoginBody = { email: string; password: string };
type LoginResponse = { accessToken: string };

type RegisterBody = { email: string; password: string };
type RegisterResponse = { detail: string };

type VerifyResponse = { detail?: string } | string;

type ResetStartBody = { email: string };
type ResetEndBody = { token: string; newPassword: string };
type DetailResponse = { detail: string };

export const authApi = {

    
    async resetStart(email: string) {
  const { data } = await http.post<DetailResponse>("/auth/reset/start", { email } as ResetStartBody);
  return data;
  
},



async resetEnd(token: string, newPassword: string) {
  const { data } = await http.post<DetailResponse>("/auth/reset/end", { token, newPassword } as ResetEndBody);
  return data;
},
sendVerification: (email: string) =>
  http.post("/auth/verification/send", { email }),

  async login(body: LoginBody) {
    const { data } = await http.post<LoginResponse>("/auth/login", body);
    tokenStorage.set(data.accessToken);
    return data;
  },

  async register(body: RegisterBody) {
    // тут токены не нужны, бэк отвечает "Операция успешно выполнена!"
    const { data } = await http.post<RegisterResponse>("/auth/register", body);
    return data;
  },

  async verifyEmail(token: string) {
    const { data } = await http.post<VerifyResponse>("/auth/verification", { token });
    return data;
  },

  async logout() {
    // бэк должен чистить refresh cookie
    await http.post("/auth/logout", {});
    tokenStorage.clear();
  },

  async refresh() {
    const { data } = await http.post<LoginResponse>("/auth/refresh", {});
    tokenStorage.set(data.accessToken);
    return data;
  },
  
};