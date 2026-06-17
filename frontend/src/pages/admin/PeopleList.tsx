import { useEffect, useState } from "react";
import { peopleApi } from "../../api/peopleApi";
import type { Person } from "../../api/types";
import { formatShort } from "../../utils/date";
import { enumLabel } from "../../utils/labels";
import { getApiErrorMessage } from "../../utils/errors";
import "../../styles/admin.css";

const calcAge = (iso: string): string => {
  if (!iso) return "";
  const b = new Date(iso);
  if (isNaN(b.getTime())) return "";
  const t = new Date();
  let age = t.getFullYear() - b.getFullYear();
  const m = t.getMonth() - b.getMonth();
  if (m < 0 || (m === 0 && t.getDate() < b.getDate())) age--;
  return String(age);
};

export default function PeopleList() {
  const [people, setPeople] = useState<Person[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const data = await peopleApi.getAll();
        setPeople(Array.isArray(data) ? data : []);
      } catch (e) {
        console.error(e);
        alert(getApiErrorMessage(e, "Ошибка загрузки списка людей"));
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const filtered = people.filter((p) => p.name.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="admin">
      <div className="admin-header">
        <div className="admin-title">Люди (личные данные)</div>
        <input
          className="admin-search"
          placeholder="Поиск по имени..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>
      <p className="admin-muted" style={{ marginTop: -8, marginBottom: 14 }}>
        Только просмотр: бэкенд не отдаёт измерения по id — анализы доступны человеку в его кабинете.
      </p>

      {loading ? (
        <div className="admin-loading">Загрузка...</div>
      ) : (
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th style={{ width: 60 }}>ID</th>
                <th>Имя</th>
                <th>Пол</th>
                <th>Дата рожд.</th>
                <th>Возраст</th>
                <th>Состояние</th>
                <th>Исключённые причины</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.name}</td>
                  <td>{enumLabel(p.gender)}</td>
                  <td>{formatShort(p.dateOfBirth)}</td>
                  <td>{calcAge(p.dateOfBirth)}</td>
                  <td>{enumLabel(p.condition)}</td>
                  <td>
                    {p.excludedReasons?.length ? (
                      p.excludedReasons.map((r) => (
                        <span key={r.id} className="chip">{r.name}</span>
                      ))
                    ) : (
                      <span className="admin-muted">—</span>
                    )}
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
