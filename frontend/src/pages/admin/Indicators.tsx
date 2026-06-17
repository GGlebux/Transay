import { Fragment, useEffect, useMemo, useState } from "react";
import type { FormEvent } from "react";
import { indicatorsApi } from "../../api/indicatorsApi";
import { unitsApi } from "../../api/unitsApi";
import type { Indicator, IndicatorPayload, IndicatorGender, Condition, AgeRange } from "../../api/types";
import { enumLabel } from "../../utils/labels";
import "../../styles/admin.css";

const GENDERS: IndicatorGender[] = ["MALE", "FEMALE", "BOTH"];
const CONDITIONS: Condition[] = ["BASE", "GRAVID", "MENSES"];

const emptyAge = (): AgeRange => ({ years: 0, months: 0, days: 0 });
const emptyForm = (): IndicatorPayload => ({
  engName: "",
  rusName: "",
  gender: "BOTH",
  condition: "BASE",
  minAge: emptyAge(),
  maxAge: emptyAge(),
  minValue: 0,
  maxValue: 0,
  units: "",
});

const ageToStr = (a: AgeRange): string =>
  [a.years ? `${a.years}г` : "", a.months ? `${a.months}м` : "", a.days ? `${a.days}д` : ""]
    .filter(Boolean)
    .join(" ") || "0";

export default function Indicators() {
  const [items, setItems] = useState<Indicator[]>([]);
  const [units, setUnits] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  const [open, setOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [form, setForm] = useState<IndicatorPayload>(emptyForm());
  const [saving, setSaving] = useState(false);
  const [expanded, setExpanded] = useState<Set<string>>(new Set());

  const toggleGroup = (name: string) =>
    setExpanded((prev) => {
      const next = new Set(prev);
      if (next.has(name)) next.delete(name);
      else next.add(name);
      return next;
    });

  const load = async () => {
    setLoading(true);
    try {
      const [inds, us] = await Promise.all([
        indicatorsApi.getAll(),
        unitsApi.getAll().catch(() => []),
      ]);
      setItems([...inds].sort((a, b) => a.rusName.localeCompare(b.rusName, "ru")));
      setUnits(us.map((u) => u.name));
    } catch (e) {
      console.error(e);
      alert("Ошибка загрузки индикаторов");
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

  const openEdit = (it: Indicator) => {
    setEditingId(it.id);
    setForm({
      engName: it.engName,
      rusName: it.rusName,
      gender: it.gender,
      condition: it.condition,
      minAge: { ...it.minAge },
      maxAge: { ...it.maxAge },
      minValue: it.minValue,
      maxValue: it.maxValue,
      units: it.units ?? "",
    });
    setOpen(true);
  };

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    if (!form.engName.trim() || !form.rusName.trim()) {
      alert("Заполните английское и русское название");
      return;
    }
    setSaving(true);
    try {
      if (editingId == null) await indicatorsApi.create(form);
      else await indicatorsApi.update(editingId, form);
      setOpen(false);
      await load();
    } catch (err: any) {
      alert(err?.response?.data?.detail || err?.message || "Ошибка сохранения");
    } finally {
      setSaving(false);
    }
  };

  const remove = async (it: Indicator) => {
    if (!window.confirm(`Удалить индикатор «${it.rusName}»?`)) return;
    try {
      await indicatorsApi.remove(it.id);
      setItems((prev) => prev.filter((x) => x.id !== it.id));
    } catch (err: any) {
      alert(err?.response?.data?.detail || err?.message || "Ошибка удаления");
    }
  };

  const setAge = (key: "minAge" | "maxAge", part: keyof AgeRange, v: string) =>
    setForm((f) => ({ ...f, [key]: { ...f[key], [part]: Number(v) || 0 } }));

  const filtered = useMemo(
    () =>
      items.filter(
        (i) =>
          i.rusName.toLowerCase().includes(search.toLowerCase()) ||
          i.engName.toLowerCase().includes(search.toLowerCase())
      ),
    [items, search]
  );

  // Схлопываем индикаторы с одинаковым русским названием в одну раскрывающуюся группу
  const groups = useMemo(() => {
    const map = new Map<string, Indicator[]>();
    for (const it of filtered) {
      const arr = map.get(it.rusName);
      if (arr) arr.push(it);
      else map.set(it.rusName, [it]);
    }
    return [...map.entries()]
      .sort((a, b) => a[0].localeCompare(b[0], "ru"))
      .map(([rusName, list]) => ({ rusName, list }));
  }, [filtered]);

  const renderRow = (it: Indicator, child = false) => (
    <tr key={it.id} className={child ? "ind-child-row" : undefined}>
      <td>{it.id}</td>
      <td className={child ? "ind-child-name" : undefined}>{it.rusName}</td>
      <td className="admin-muted">{it.engName}</td>
      <td>{enumLabel(it.gender)}</td>
      <td>{enumLabel(it.condition)}</td>
      <td className="admin-muted">{ageToStr(it.minAge)} – {ageToStr(it.maxAge)}</td>
      <td>{it.minValue} – {it.maxValue}</td>
      <td>{it.units || "—"}</td>
      <td>
        <div className="admin-actions">
          <button className="icon-btn" onClick={() => openEdit(it)}>✏️</button>
          <button className="icon-btn icon-btn--danger" onClick={() => remove(it)}>🗑</button>
        </div>
      </td>
    </tr>
  );

  return (
    <div className="admin">
      <div className="admin-header">
        <div className="admin-title">Индикаторы</div>
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
          <table className="admin-table admin-table--wide">
            <thead>
              <tr>
                <th style={{ width: 50 }}>ID</th>
                <th>Рус. название</th>
                <th>Eng</th>
                <th>Пол</th>
                <th>Сост.</th>
                <th>Возраст (мин–макс)</th>
                <th>Норма</th>
                <th>Ед.</th>
                <th style={{ width: 110 }}>Действия</th>
              </tr>
            </thead>
            <tbody>
              {groups.map((g) =>
                g.list.length === 1 ? (
                  renderRow(g.list[0])
                ) : (
                  <Fragment key={g.rusName}>
                    <tr className="ind-group-row" onClick={() => toggleGroup(g.rusName)}>
                      <td className="ind-group-toggle">{expanded.has(g.rusName) ? "▾" : "▸"}</td>
                      <td>
                        <span className="ind-group-name">{g.rusName}</span>
                        <span className="ind-group-badge">{g.list.length}</span>
                      </td>
                      <td className="admin-muted" colSpan={6}>
                        {g.list.length} вариантов по полу / возрасту / состоянию — нажмите, чтобы раскрыть
                      </td>
                      <td></td>
                    </tr>
                    {expanded.has(g.rusName) && g.list.map((it) => renderRow(it, true))}
                  </Fragment>
                )
              )}
              {groups.length === 0 && (
                <tr>
                  <td colSpan={9} className="admin-empty">Ничего не найдено</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {open && (
        <div className="admin-modal-backdrop" onClick={() => setOpen(false)}>
          <form className="admin-modal" onClick={(e) => e.stopPropagation()} onSubmit={submit}>
            <h3>{editingId == null ? "Новый индикатор" : "Редактировать индикатор"}</h3>

            <div className="admin-grid2">
              <div className="admin-field">
                <label>Русское название *</label>
                <input value={form.rusName} onChange={(e) => setForm({ ...form, rusName: e.target.value })} />
              </div>
              <div className="admin-field">
                <label>Английское название *</label>
                <input value={form.engName} onChange={(e) => setForm({ ...form, engName: e.target.value })} />
              </div>
            </div>

            <div className="admin-grid2">
              <div className="admin-field">
                <label>Пол</label>
                <select value={form.gender} onChange={(e) => setForm({ ...form, gender: e.target.value as IndicatorGender })}>
                  {GENDERS.map((g) => <option key={g} value={g}>{enumLabel(g)}</option>)}
                </select>
              </div>
              <div className="admin-field">
                <label>Состояние</label>
                <select value={form.condition} onChange={(e) => setForm({ ...form, condition: e.target.value as Condition })}>
                  {CONDITIONS.map((c) => <option key={c} value={c}>{enumLabel(c)}</option>)}
                </select>
              </div>
            </div>

            <div className="admin-field">
              <label>Минимальный возраст (годы / месяцы / дни)</label>
              <div className="admin-grid3">
                <input type="number" value={form.minAge.years} onChange={(e) => setAge("minAge", "years", e.target.value)} placeholder="годы" />
                <input type="number" value={form.minAge.months} onChange={(e) => setAge("minAge", "months", e.target.value)} placeholder="месяцы" />
                <input type="number" value={form.minAge.days} onChange={(e) => setAge("minAge", "days", e.target.value)} placeholder="дни" />
              </div>
            </div>

            <div className="admin-field">
              <label>Максимальный возраст (годы / месяцы / дни)</label>
              <div className="admin-grid3">
                <input type="number" value={form.maxAge.years} onChange={(e) => setAge("maxAge", "years", e.target.value)} placeholder="годы" />
                <input type="number" value={form.maxAge.months} onChange={(e) => setAge("maxAge", "months", e.target.value)} placeholder="месяцы" />
                <input type="number" value={form.maxAge.days} onChange={(e) => setAge("maxAge", "days", e.target.value)} placeholder="дни" />
              </div>
            </div>

            <div className="admin-grid3">
              <div className="admin-field">
                <label>Нижний порог нормы</label>
                <input type="number" step="any" value={form.minValue} onChange={(e) => setForm({ ...form, minValue: Number(e.target.value) })} />
              </div>
              <div className="admin-field">
                <label>Верхний порог нормы</label>
                <input type="number" step="any" value={form.maxValue} onChange={(e) => setForm({ ...form, maxValue: Number(e.target.value) })} />
              </div>
              <div className="admin-field">
                <label>Единицы</label>
                <input list="units-list" value={form.units ?? ""} onChange={(e) => setForm({ ...form, units: e.target.value })} />
                <datalist id="units-list">
                  {units.map((u) => <option key={u} value={u} />)}
                </datalist>
              </div>
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
