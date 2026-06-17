import { useEffect, useState } from "react";
import { customersApi } from "../../api/customersApi";
import type { Customer, CustomerRole } from "../../api/types";
import { enumLabel } from "../../utils/labels";
import { getApiErrorMessage } from "../../utils/errors";
import "../../styles/admin.css";

const ROLES: CustomerRole[] = ["USER", "EDITOR", "ADMIN"];

export default function Users() {
  const [users, setUsers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [busyId, setBusyId] = useState<number | null>(null);

  const load = async () => {
    setLoading(true);
    try {
      const data = await customersApi.getAll();
      setUsers([...data].sort((a, b) => a.id - b.id));
    } catch (e) {
      console.error(e);
      alert(getApiErrorMessage(e, "Ошибка загрузки пользователей"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const changeRole = async (user: Customer, role: CustomerRole) => {
    if (role === user.role) return;
    if (!window.confirm(`Сменить роль ${user.email}: ${user.role} → ${role}?`)) return;
    setBusyId(user.id);
    try {
      await customersApi.upgrade(user.id, role);
      setUsers((prev) => prev.map((u) => (u.id === user.id ? { ...u, role } : u)));
    } catch (err: any) {
      alert(getApiErrorMessage(err, "Ошибка смены роли"));
    } finally {
      setBusyId(null);
    }
  };

  const filtered = users.filter((u) => u.email.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="admin">
      <div className="admin-header">
        <div className="admin-title">Пользователи</div>
        <input
          className="admin-search"
          placeholder="Поиск по email..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button className="admin-btn admin-btn--ghost" onClick={load}>Обновить</button>
      </div>

      {loading ? (
        <div className="admin-loading">Загрузка...</div>
      ) : (
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th style={{ width: 60 }}>ID</th>
                <th>Email</th>
                <th>Статус</th>
                <th>Подтв.</th>
                <th>Профиль</th>
                <th>Создан</th>
                <th style={{ width: 230 }}>Роль</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((u) => (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td>{u.email}</td>
                  <td><span className="admin-muted">{enumLabel(u.status)}</span></td>
                  <td>
                    <span className={`status-badge ${u.verified ? "ok" : "no"}`}>
                      {u.verified ? "✓" : "✗"}
                    </span>
                  </td>
                  <td>{u.personId ?? <span className="admin-muted">—</span>}</td>
                  <td className="admin-muted">{String(u.createdAt).slice(0, 10)}</td>
                  <td>
                    <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
                      <span className={`role-pill role-${u.role}`}>{enumLabel(u.role)}</span>
                      <select
                        className="admin-search"
                        style={{ minWidth: 110 }}
                        value={u.role}
                        disabled={busyId === u.id}
                        onChange={(e) => changeRole(u, e.target.value as CustomerRole)}
                      >
                        {ROLES.map((r) => (
                          <option key={r} value={r}>{enumLabel(r)}</option>
                        ))}
                      </select>
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan={7} className="admin-empty">Ничего не найдено</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
