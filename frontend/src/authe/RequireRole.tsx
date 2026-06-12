import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

type Role = "ADMIN" | "EDITOR" | "USER";

export const RequireRole = ({
  roleRequired,
  children,
}: {
  roleRequired: Role | Role[];
  children: React.ReactNode;
}) => {
  const { role, loading } = useAuth();

  if (loading) return null;

  const allowed = Array.isArray(roleRequired) ? roleRequired : [roleRequired];
  if (!role || !allowed.includes(role)) {
    return <Navigate to="/cabinet" replace />;
  }

  return <>{children}</>;
};