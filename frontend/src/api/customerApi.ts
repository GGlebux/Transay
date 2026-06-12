import { http } from "./http";

export const customerApi = {
  getMe: async () => {
    const { data } = await http.get("/customers");
    return data;
  },
};