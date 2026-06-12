import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

export const RequireRole = ({
  roleRequired,
  children,
}: {
  roleRequired: "ADMIN" | "EDITOR" | "USER";
  children: React.ReactNode;
}) => {
  const { role, loading } = useAuth();

  if (loading) return null;

  if (role !== roleRequired) {
    return <Navigate to="/cabinet" replace />;
  }

  return <>{children}</>;
};