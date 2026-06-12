import { NavLink, Outlet } from "react-router-dom";
import "../../styles/admin.css";

const TABS = [
  { to: "/admin/users", label: "Пользователи" },
  { to: "/admin/people", label: "Люди" },
  { to: "/admin/indicators", label: "Индикаторы" },
  { to: "/admin/groups", label: "Группы" },
  { to: "/admin/transcripts", label: "Транскрипции" },
  { to: "/admin/reasons", label: "Причины" },
  { to: "/admin/units", label: "Единицы" },
];

export default function AdminLayout() {
  return (
    <div className="admin">
      <nav className="admin-tabs">
        {TABS.map((t) => (
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
