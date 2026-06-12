import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../../authe/AuthContext";
import "../../styles/admin.css";

const TABS = [
  { to: "/admin/users", label: "Пользователи", adminOnly: true },
  { to: "/admin/people", label: "Люди", adminOnly: true },
  { to: "/admin/indicators", label: "Индикаторы", adminOnly: false },
  { to: "/admin/groups", label: "Группы", adminOnly: false },
  { to: "/admin/transcripts", label: "Транскрипции", adminOnly: false },
  { to: "/admin/reasons", label: "Причины", adminOnly: false },
  { to: "/admin/units", label: "Единицы", adminOnly: false },
];

export default function AdminLayout() {
  const { role } = useAuth();
  const tabs = TABS.filter((t) => !t.adminOnly || role === "ADMIN");

  return (
    <div className="admin">
      <nav className="admin-tabs">
        {tabs.map((t) => (
          <NavLink
            key={t.to}
            to={t.to}
            className={({ isActive }) => (isActive ? "admin-tab active" : "admin-tab")}
          >
            {t.label}
          </NavLink>
        ))}
      </nav>
      <Outlet />
    </div>
  );
}
