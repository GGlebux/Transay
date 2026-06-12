import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { groupsApi } from "../../api/groupsApi";
import type { IndicatorGroup, IndicatorGroupPayload, SimpleIndicator } from "../../api/types";
import "../../styles/admin.css";

const emptyForm = (): IndicatorGroupPayload => ({ groupName: "", indicators: [{ name: "", units: [] }] });

export default function Groups() {
  const [items, setItems] = useState<IndicatorGroup[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  const [open, setOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [form, setForm] = useState<IndicatorGroupPayload>(emptyForm());
  const [saving, setSaving] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const data = await groupsApi.getAll();
      setItems([...data].sort((a, b) => a.groupName.localeCompare(b.groupName, "ru")));
    } catch (e) {
      console.error(e);
      alert("Ошибка загрузки групп");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const openCreate = () => {
    setEditingId(null);
    setForm(emptyForm());
    setOpen(true);
  };

  const openEdit = (g: IndicatorGroup) => {
    setEditingId(g.id);
    setForm({
      groupName: g.groupName,
      indicators: g.indicators.length
        ? g.indicators.map((i) => ({ name: i.name, units: [...(i.units ?? [])] }))
        : [{ name: "", units: [] }],
    });
    setOpen(true);
  };

  const setIndicator = (idx: number, patch: Partial<SimpleIndicator>) =>
    setForm((f) => ({
      ...f,
      indicators: f.indicators.map((it, i) => (i === idx ? { ...it, ...patch } : it)),
    }));

  const addIndicator = () => setForm((f) => ({ ...f, indicators: [...f.indicators, { name: "", units: [] }] }));
  const removeIndicator = (idx: number) =>
    setForm((f) => ({ ...f, indicators: f.indicators.filter((_, i) => i !== idx) }));

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    if (!form.groupName.trim()) return alert("Укажите название группы");
    const indicators = form.indicators
      .map((i) => ({ name: i.name.trim(), units: i.units.filter(Boolean) }))
      .filter((i) => i.name);
    if (indicators.length === 0) return alert("Добавьте хотя бы один индикатор");

    setSaving(true);
    try {
      const payload: IndicatorGroupPayload = { groupName: form.groupName.trim(), indicators };
      if (editingId == null) await groupsApi.create(payload);
      else await groupsApi.update(editingId, payload);
      setOpen(false);
      await load();
    } catch (err: any) {
      alert(err?.response?.data?.detail || err?.message || "Ошибка сохранения");
    } finally {
      setSaving(false);
    }
  };

  const remove = async (g: IndicatorGroup) => {
    if (!window.confirm(`Удалить группу «${g.groupName}»?`)) return;
    try {
      await groupsApi.remove(g.id);
      setItems((prev) => prev.filter((x) => x.id !== g.id));
    } catch (err: any) {
      alert(err?.response?.data?.detail || err?.message || "Ошибка удаления");
    }
  };

  const filtered = items.filter((g) => g.groupName.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="admin">
      <div className="admin-header">
        <div className="admin-title">Группы индикаторов</div>
        <input
          className="admin-search"
          placeholder="Поиск по названию..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button className="admin-btn" onClick={openCreate}>Создать +</button>
      </div>

      {loading ? (
        <div className="admin-loading">Загрузка...</div>
      ) : (
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th style={{ width: 50 }}>ID</th>
                <th>Название группы</th>
                <th>Индикаторы</th>
                <th style={{ width: 110 }}>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((g) => (
                <tr key={g.id}>
                  <td>{g.id}</td>
                  <td>{g.groupName}</td>
                  <td>
                    {g.indicators.map((i) => (
                      <span key={i.name} className="chip">
                        {i.name}{i.units?.length ? ` · ${i.units.join("/")}` : ""}
                      </span>
                    ))}
                  </td>
                  <td>
                    <div className="admin-actions">
                      <button className="icon-btn" onClick={() => openEdit(g)}>✏️</button>
                      <button className="icon-btn icon-btn--danger" onClick={() => remove(g)}>🗑</button>
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan={4} className="admin-empty">Ничего не найдено</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {open && (
        <div className="admin-modal-backdrop" onClick={() => setOpen(false)}>
          <form className="admin-modal" onClick={(e) => e.stopPropagation()} onSubmit={submit}>
            <h3>{editingId == null ? "Новая группа" : "Редактировать группу"}</h3>

            <div className="admin-field">
              <label>Название группы *</label>
              <input value={form.groupName} onChange={(e) => setForm({ ...form, groupName: e.target.value })} />
            </div>

            <label className="admin-field" style={{ marginBottom: 6 }}>Индикаторы</label>
            {form.indicators.map((it, idx) => (
              <div className="indicator-row" key={idx}>
                <input
                  placeholder="Название индикатора"
                  value={it.name}
                  onChange={(e) => setIndicator(idx, { name: e.target.value })}
                />
                <input
                  placeholder="Единицы через запятую (г/л, %)"
                  value={it.units.join(", ")}
                  onChange={(e) => setIndicator(idx, { units: e.target.value.split(",").map((s) => s.trim()).filter(Boolean) })}
                />
                <button type="button" className="icon-btn icon-btn--danger" onClick={() => removeIndicator(idx)} title="Удалить строку">×</button>
              </div>
            ))}
            <button type="button" className="admin-btn admin-btn--ghost" onClick={addIndicator} style={{ marginTop: 4 }}>
              + Индикатор
            </button>

            <div className="admin-modal-actions">
              <button type="button" className="admin-btn admin-btn--ghost" onClick={() => setOpen(false)}>Отмена</button>
              <button type="submit" className="admin-btn" disabled={saving}>{saving ? "Сохранение..." : "Сохранить"}</button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}
