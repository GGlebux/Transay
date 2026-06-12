import { NavLink } from "react-router-dom";
import "../../styles/layout.css";
import { useAuth } from "../../authe/AuthContext";
import User from "../../assets/User.png";
import Users from "../../assets/Users.png";
import Forms from "../../assets/Forms.svg";
import logo from "../../assets/Logo.png";

type Props = {
  isOpen?: boolean;
  onClose?: () => void;
};

export default function Sidebar({ isOpen = false, onClose }: Props) {
  const { role } = useAuth();

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
            <li>
              <NavLink to="/measures" onClick={onClose} className={({ isActive }) => (isActive ? "active" : "")}>
                <img src={Users} alt="" /> Мои анализы
              </NavLink>
            </li>
            {role === "ADMIN" && (
              <li>
                <NavLink to="/admin" onClick={onClose} className={({ isActive }) => (isActive ? "active" : "")}>
                  <img src={Forms} alt="" /> Админка
                </NavLink>
              </li>
            )}
          </ul>
        </nav>
      </aside>
    </>
  );
}
