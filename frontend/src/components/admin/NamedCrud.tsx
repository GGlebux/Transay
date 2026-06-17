import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { getApiErrorMessage } from "../../utils/errors";
import { EditIcon, TrashIcon } from "../icons/Icons";
import "../../styles/admin.css";

type Named = { id: number; name: string };

type NamedApi = {
  getAll: () => Promise<Named[]>;
  create: (name: string) => Promise<Named>;
  update: (id: number, name: string) => Promise<Named>;
  remove: (id: number) => Promise<void>;
};

// Универсальный CRUD для сущностей вида {id, name} (причины, единицы измерения)
export default function NamedCrud({
  title,
  api,
  placeholder,
}: {
  title: string;
  api: NamedApi;
  placeholder: string;
}) {
  const [items, setItems] = useState<Named[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [creating, setCreating] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editValue, setEditValue] = useState("");
  const [busyId, setBusyId] = useState<number | null>(null);

  const load = async () => {
    setLoading(true);
    try {
      const data = await api.getAll();
      setItems([...data].sort((a, b) => a.name.localeCompare(b.name, "ru")));
    } catch (e) {
      console.error(e);
      alert(getApiErrorMessage(e, "Ошибка загрузки списка"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const create = async (e: FormEvent) => {
    e.preventDefault();
    if (!value.trim()) return;
    setCreating(true);
    try {
      await api.create(value.trim());
      setValue("");
      await load();
    } catch (err: any) {
      alert(getApiErrorMessage(err, "Ошибка создания"));
    } finally {
      setCreating(false);
    }
  };

  const saveEdit = async (id: number) => {
    if (!editValue.trim()) return;
    setBusyId(id);
    try {
      await api.update(id, editValue.trim());
      setEditingId(null);
      await load();
    } catch (err: any) {
      alert(getApiErrorMessage(err, "Ошибка сохранения"));
    } finally {
      setBusyId(null);
    }
  };

  const remove = async (item: Named) => {
    if (!window.confirm(`Удалить «${item.name}»?`)) return;
    setBusyId(item.id);
    try {
      await api.remove(item.id);
      setItems((prev) => prev.filter((x) => x.id !== item.id));
    } catch (err: any) {
      alert(getApiErrorMessage(err, "Ошибка удаления"));
    } finally {
      setBusyId(null);
    }
  };

  const filtered = items.filter((i) => i.name.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="admin">
      <div className="admin-header">
        <div className="admin-title">{title}</div>
        <input
          className="admin-search"
          placeholder="Поиск..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <form onSubmit={create} style={{ display: "flex", gap: 10, marginBottom: 16 }}>
        <input
          className="admin-search"
          placeholder={placeholder}
          value={value}
          onChange={(e) => setValue(e.target.value)}
        />
        <button className="admin-btn" type="submit" disabled={creating || !value.trim()}>
          {creating ? "..." : "Добавить +"}
        </button>
      </form>

      {loading ? (
        <div className="admin-loading">Загрузка...</div>
      ) : (
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th style={{ width: 70 }}>ID</th>
                <th>Название</th>
                <th style={{ width: 180 }}>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td>
                    {editingId === item.id ? (
                      <input
                        className="admin-search"
                        value={editValue}
                        autoFocus
                        onChange={(e) => setEditValue(e.target.value)}
                        onKeyDown={(e) => e.key === "Enter" && saveEdit(item.id)}
                      />
                    ) : (
                      item.name
                    )}
                  </td>
                  <td>
                    <div className="admin-actions">
                      {editingId === item.id ? (
                        <>
                          <button className="icon-btn" disabled={busyId === item.id} onClick={() => saveEdit(item.id)}>💾 Сохранить</button>
                          <button className="icon-btn" onClick={() => setEditingId(null)}>Отмена</button>
                        </>
                      ) : (
                        <>
                          <button
                            className="icon-btn"
                            title="Изменить"
                            aria-label="Изменить"
                            onClick={() => {
                              setEditingId(item.id);
                              setEditValue(item.name);
                            }}
                          >
                            <EditIcon />
                          </button>
                          <button className="icon-btn icon-btn--danger" disabled={busyId === item.id} onClick={() => remove(item)} title="Удалить" aria-label="Удалить"><TrashIcon /></button>
                        </>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan={3} className="admin-empty">Ничего не найдено</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
