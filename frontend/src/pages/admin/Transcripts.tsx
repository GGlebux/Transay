import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { transcriptsApi } from "../../api/transcriptsApi";
import { reasonsApi } from "../../api/reasonsApi";
import { MultiSelectWithSearch } from "../../components/Trans_Indicat/MultiSelectWithSearch";
import type { Transcript, TranscriptPayload, IndicatorGender, Reason } from "../../api/types";
import { enumLabel } from "../../utils/labels";
import "../../styles/admin.css";

const GENDERS: IndicatorGender[] = ["MALE", "FEMALE", "BOTH"];

const emptyForm = (): TranscriptPayload => ({ name: "", gender: "BOTH", fallsIds: [], raisesIds: [] });

export default function Transcripts() {
  const [items, setItems] = useState<Transcript[]>([]);
  const [reasons, setReasons] = useState<Reason[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  const [open, setOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [form, setForm] = useState<TranscriptPayload>(emptyForm());
  const [saving, setSaving] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const [trans, rs] = await Promise.all([
        transcriptsApi.getAll(),
        reasonsApi.getAll().catch(() => []),
      ]);
      setItems([...trans].sort((a, b) => a.name.localeCompare(b.name, "ru")));
      setReasons(rs);
    } catch (e) {
      console.error(e);
      alert("Ошибка загрузки транскрипций");
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

  const openEdit = (t: Transcript) => {
    setEditingId(t.id);
    setForm({
      name: t.name,
      gender: t.gender,
      fallsIds: (t.falls ?? []).map((r) => r.id),
      raisesIds: (t.raises ?? []).map((r) => r.id),
    });
    setOpen(true);
  };

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    if (!form.name.trim()) return alert("Укажите имя индикатора");
    setSaving(true);
    try {
      if (editingId == null) await transcriptsApi.create(form);
      else await transcriptsApi.update(editingId, form);
      setOpen(false);
      await load();
    } catch (err: any) {
      alert(err?.response?.data?.detail || err?.message || "Ошибка сохранения");
    } finally {
      setSaving(false);
    }
  };

  const remove = async (t: Transcript) => {
    if (!window.confirm(`Удалить транскрипцию «${t.name}»?`)) return;
    try {
      await transcriptsApi.remove(t.id);
      setItems((prev) => prev.filter((x) => x.id !== t.id));
    } catch (err: any) {
      alert(err?.response?.data?.detail || err?.message || "Ошибка удаления");
    }
  };

  const filtered = items.filter((t) => t.name.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="admin">
      <div className="admin-header">
        <div className="admin-title">Транскрипции</div>
        <input
          className="admin-search"
          placeholder="Поиск по имени..."
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
                <th>Имя индикатора</th>
                <th>Пол</th>
                <th>Причины понижения</th>
                <th>Причины повышения</th>
                <th style={{ width: 110 }}>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((t) => (
                <tr key={t.id}>
                  <td>{t.id}</td>
                  <td>{t.name}</td>
                  <td>{enumLabel(t.gender)}</td>
                  <td>
                    {t.falls?.length ? (
                      t.falls.map((r) => <span key={r.id} className="chip">{r.name}</span>)
                    ) : (
                      <span className="admin-muted">—</span>
                    )}
                  </td>
                  <td>
                    {t.raises?.length ? (
                      t.raises.map((r) => <span key={r.id} className="chip">{r.name}</span>)
                    ) : (
                      <span className="admin-muted">—</span>
                    )}
                  </td>
                  <td>
                    <div className="admin-actions">
                      <button className="icon-btn" onClick={() => openEdit(t)}>✏️</button>
                      <button className="icon-btn icon-btn--danger" onClick={() => remove(t)}>🗑</button>
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan={6} className="admin-empty">Ничего не найдено</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {open && (
        <div className="admin-modal-backdrop" onClick={() => setOpen(false)}>
          <form className="admin-modal" onClick={(e) => e.stopPropagation()} onSubmit={submit}>
            <h3>{editingId == null ? "Новая транскрипция" : "Редактировать транскрипцию"}</h3>

            <div className="admin-grid2">
              <div className="admin-field">
                <label>Имя индикатора (eng_name) *</label>
                <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
              </div>
              <div className="admin-field">
                <label>Пол</label>
                <select value={form.gender} onChange={(e) => setForm({ ...form, gender: e.target.value as IndicatorGender })}>
                  {GENDERS.map((g) => <option key={g} value={g}>{enumLabel(g)}</option>)}
                </select>
              </div>
            </div>

            <div className="admin-field">
              <label>Причины понижения</label>
              <MultiSelectWithSearch
                label="Выберите причины понижения"
                options={reasons}
                selected={form.fallsIds}
                onChange={(ids) => setForm({ ...form, fallsIds: ids })}
              />
              <div>{form.fallsIds.map((id) => {
                const r = reasons.find((x) => x.id === id);
                return r ? <span key={id} className="chip">{r.name}</span> : null;
              })}</div>
            </div>

            <div className="admin-field">
              <label>Причины повышения</label>
              <MultiSelectWithSearch
                label="Выберите причины повышения"
                options={reasons}
                selected={form.raisesIds}
                onChange={(ids) => setForm({ ...form, raisesIds: ids })}
              />
              <div>{form.raisesIds.map((id) => {
                const r = reasons.find((x) => x.id === id);
                return r ? <span key={id} className="chip">{r.name}</span> : null;
              })}</div>
            </div>

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
