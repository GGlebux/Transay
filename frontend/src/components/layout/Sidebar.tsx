import { NavLink } from "react-router-dom";
import "../../styles/layout.css";
import { useAuth } from "../../authe/AuthContext";
import User from "../../assets/User.png";
import Users from "../../assets/Users.png";
import logo from "../../assets/Logo.png";

type Props = {
  isOpen?: boolean;
  onClose?: () => void;
};

const ADMIN_NAV = [
  { to: "/admin/users", label: "Пользователи", icon: "👥", adminOnly: true },
  { to: "/admin/people", label: "Люди", icon: "🧑", adminOnly: true },
  { to: "/admin/indicators", label: "Индикаторы", icon: "🧪", adminOnly: false },
  { to: "/admin/groups", label: "Группы", icon: "🗂️", adminOnly: false },
  { to: "/admin/transcripts", label: "Транскрипции", icon: "📝", adminOnly: false },
  { to: "/admin/reasons", label: "Причины", icon: "⚠️", adminOnly: false },
  { to: "/admin/units", label: "Единицы", icon: "📐", adminOnly: false },
  { to: "/admin/system", label: "Мониторинг", icon: "📊", adminOnly: true },
];

export default function Sidebar({ isOpen = false, onClose }: Props) {
  const { role } = useAuth();
  const isStaff = role === "ADMIN" || role === "EDITOR";
  const adminNav = ADMIN_NAV.filter((t) => !t.adminOnly || role === "ADMIN");

  return (
    <>
      <div className={`sidebarOverlay ${isOpen ? "open" : ""}`} onClick={onClose} />

      <aside className={`sidebar ${isOpen ? "open" : ""}`}>
        <div className="sidebarTop">
          <img className="brandLogo" src={logo} alt="Logo" />
          <button className="sidebarClose" type="button" onClick={onClose} aria-label="Close menu">×</button>
        </div>

        <nav>
          <ul>
            <li>
              <NavLink to="/cabinet" onClick={onClose} className={({ isActive }) => (isActive ? "active" : "")}>
                <img src={User} alt="" /> Профиль
              </NavLink>
            </li>

            {role === "USER" && (
              <li>
                <NavLink to="/measures" onClick={onClose} className={({ isActive }) => (isActive ? "active" : "")}>
                  <img src={Users} alt="" /> Мои анализы
                </NavLink>
              </li>
            )}

            {isStaff && adminNav.map((t) => (
              <li key={t.to}>
                <NavLink to={t.to} onClick={onClose} className={({ isActive }) => (isActive ? "active" : "")}>
                  <span className="nav-ico" aria-hidden="true">{t.icon}</span> {t.label}
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>
      </aside>
    </>
  );
}
