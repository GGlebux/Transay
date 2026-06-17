import { NavLink } from "react-router-dom";
import type { ComponentType } from "react";
import "../../styles/layout.css";
import { useAuth } from "../../authe/AuthContext";
import User from "../../assets/User.png";
import Users from "../../assets/Users.png";
import logo from "../../assets/Logo.png";
import {
  UsersIcon,
  PersonIcon,
  TestTubeIcon,
  FolderIcon,
  DocIcon,
  WarnIcon,
  RulerIcon,
  ChartIcon,
  FamilyIcon,
  type IconProps,
} from "../icons/Icons";

type Props = {
  isOpen?: boolean;
  onClose?: () => void;
};

type NavItem = {
  to: string;
  label: string;
  Icon: ComponentType<IconProps>;
  adminOnly: boolean;
};

const ADMIN_NAV: NavItem[] = [
  { to: "/admin/users", label: "Пользователи", Icon: UsersIcon, adminOnly: true },
  { to: "/admin/people", label: "Люди", Icon: PersonIcon, adminOnly: true },
  { to: "/admin/indicators", label: "Индикаторы", Icon: TestTubeIcon, adminOnly: false },
  { to: "/admin/groups", label: "Группы", Icon: FolderIcon, adminOnly: false },
  { to: "/admin/transcripts", label: "Транскрипции", Icon: DocIcon, adminOnly: false },
  { to: "/admin/reasons", label: "Причины", Icon: WarnIcon, adminOnly: false },
  { to: "/admin/units", label: "Единицы", Icon: RulerIcon, adminOnly: false },
  { to: "/admin/system", label: "Мониторинг", Icon: ChartIcon, adminOnly: true },
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
              <>
                <li>
                  <NavLink to="/measures" onClick={onClose} className={({ isActive }) => (isActive ? "active" : "")}>
                    <img src={Users} alt="" /> Мои анализы
                  </NavLink>
                </li>
                <li>
                  <NavLink to="/family" onClick={onClose} className={({ isActive }) => (isActive ? "active" : "")}>
                    <span className="nav-ico"><FamilyIcon /></span> Семья
                  </NavLink>
                </li>
              </>
            )}

            {isStaff && adminNav.map((t) => {
              const Icon = t.Icon;
              return (
                <li key={t.to}>
                  <NavLink to={t.to} onClick={onClose} className={({ isActive }) => (isActive ? "active" : "")}>
                    <span className="nav-ico"><Icon /></span> {t.label}
                  </NavLink>
                </li>
              );
            })}
          </ul>
        </nav>
      </aside>
    </>
  );
}
