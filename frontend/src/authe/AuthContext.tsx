import { createContext, useContext, useEffect, useState } from "react";
import { tokenStorage } from "../authe/tokenStorage";
import { customerApi } from ".././api/customerApi";

type Role = "ADMIN" | "EDITOR" | "USER";

type Customer = {
  id: number;
  email: string;
  status: string;
  role: Role;
  createdAt: string;
  personId: number | null;
  verified: boolean;
};

type AuthContextType = {
  isAuthenticated: boolean;
  customer: Customer | null;
  role: Role | null;
  login: (accessToken: string) => Promise<Customer>;
  logout: () => void;
  loading: boolean;
  refreshCustomer: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [loading, setLoading] = useState(true);

  const login = async (accessToken: string) => {
  tokenStorage.set(accessToken);

  const data = await customerApi.getMe();
  setCustomer(data);

  return data; // ← ВАЖНО
};

  const refreshCustomer = async () => {
  const data = await customerApi.getMe();
  setCustomer(data);
};

  const logout = () => {
    tokenStorage.clear();
    setCustomer(null);
  };

  useEffect(() => {
    const init = async () => {
      const token = tokenStorage.get();
      if (!token) {
        setLoading(false);
        return;
      }

      try {
        const data = await customerApi.getMe();
        setCustomer(data);
      } catch {
        tokenStorage.clear();
        setCustomer(null);
      } finally {
        setLoading(false);
      }
    };

    init();
  }, []);

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated: !!customer,
        customer,
        role: customer?.role ?? null,
        login,
        logout,
        loading,
        refreshCustomer,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return context;
};