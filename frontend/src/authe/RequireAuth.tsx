import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

export const RequireAuth = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) return null;

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};