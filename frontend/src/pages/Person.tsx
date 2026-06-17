import { useState, useEffect, useMemo, useRef } from "react";
import type { FormEvent } from "react";
import { FloatingTextInput, FloatingSelect } from "../components/Trans_Indicat/FloatingTextField";
import { SearchSelect } from "../components/Trans_Indicat/SearchSelect";
import { useToast, useConfirm } from "../components/ui/Feedback";
import "../styles/person.css";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";

import { peopleApi } from "../api/peopleApi";
import { measuresApi } from "../api/measuresApi";
import { groupsApi } from "../api/groupsApi";
import { exReasonsApi } from "../api/exReasonsApi";
import type { Reason, SummaryGroup, IndicatorGroup } from "../api/types";
import { isoToServer, formatShort, todayISO } from "../utils/date";

/* ---------- Локальные типы вьюхи ---------- */
type Status = "ok" | "raise" | "fall";

type IndicatorOption = {
  key: string; // "groupName||name"
  label: string; // имя индикатора
  name: string;
  units: string[];
  groupName: string;
};

type DecryptDataItem = {
  id: number; // reasonId
  name: string; // имя причины
  value: number; // matchesCount
  indicators: string[];
};

const normStatus = (s: string | undefined): Status => {
  const v = String(s ?? "").toLowerCase();
  return v === "raise" ? "raise" : v === "fall" ? "fall" : "ok";
};

export default function Person({ personId }: { personId?: number } = {}) {
  // Если задан personId — работаем с членом семьи (эндпоинты /people/family/{id}/...),
  // иначе со «своими» данными текущего пользователя.
  const isFamily = personId != null;

  const notify = useToast();
  const confirm = useConfirm();

  const loadPerson = () =>
    isFamily ? peopleApi.getFamilyMember(personId!) : peopleApi.getMe();
  const loadSummary = () =>
    isFamily ? measuresApi.getSummaryFor(personId!) : measuresApi.getSummary();
  const createMeasure = (payload: Parameters<typeof measuresApi.create>[0]) =>
    isFamily ? measuresApi.createFor(personId!, payload) : measuresApi.create(payload);
  const updateMeasure = (id: number, payload: Parameters<typeof measuresApi.update>[1]) =>
    isFamily ? measuresApi.updateFor(personId!, id, payload) : measuresApi.update(id, payload);
  const removeMeasure = (id: number) =>
    isFamily ? measuresApi.removeFor(personId!, id) : measuresApi.remove(id);
  const decryptMeasures = (dateISO: string) =>
    isFamily ? measuresApi.decryptFor(personId!, dateISO) : measuresApi.decrypt(dateISO);
  const addExclusions = (ids: number[]) =>
    isFamily ? exReasonsApi.addFor(personId!, ids) : exReasonsApi.add(ids);

  const [personName, setPersonName] = useState<string>("");
  const [blocks, setBlocks] = useState<SummaryGroup[]>([]);
  const [loading, setLoading] = useState(true);

  const [allGroups, setAllGroups] = useState<IndicatorGroup[]>([]);
  const [extraGroups, setExtraGroups] = useState<string[]>([]);
  const [showAddGroup, setShowAddGroup] = useState(false);

  const [groupIndex, setGroupIndex] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const DESKTOP_COLS = 9;
  const MOBILE_COLS = 3;
  const [columnsPerPage, setColumnsPerPage] = useState(
    window.innerWidth <= 765 ? MOBILE_COLS : DESKTOP_COLS
  );

  useEffect(() => {
    const onResize = () => {
      const next = window.innerWidth <= 765 ? MOBILE_COLS : DESKTOP_COLS;
      setColumnsPerPage((prev) => (prev === next ? prev : next));
    };
    window.addEventListener("resize", onResize);
    return () => window.removeEventListener("resize", onResize);
  }, []);

  useEffect(() => {
    setCurrentPage(0);
  }, [columnsPerPage]);

  // Форма добавления измерения
  const [indicatorOptions, setIndicatorOptions] = useState<IndicatorOption[]>([]);
  const [indicatorKey, setIndicatorKey] = useState<string>("");
  const [unit, setUnit] = useState<string>("");
  const [value, setValue] = useState<string>("");
  const [date, setDate] = useState(todayISO());
  const [saving, setSaving] = useState(false);

  // Расшифровка
  const [decryptDate, setDecryptDate] = useState("");
  const [loadingDecrypt, setLoadingDecrypt] = useState(false);
  const [hoveredReasonId, setHoveredReasonId] = useState<number | null>(null);
  const [loadingExclusion, setLoadingExclusion] = useState(false);
  const [decryptData, setDecryptData] = useState<{
    largeValues: DecryptDataItem[];
    smallValues: DecryptDataItem[];
  }>({ largeValues: [], smallValues: [] });

  const COLORS = ["#f00", "#f09", "#fb00ff", "#9d00ff", "#775dd0", "#30f", "#00a2ff", "rgba(4, 68, 25, 1)", "rgba(82, 0, 150, 1)", "#fa0"];

  // Инфо-панель
  const [infoOpen, setInfoOpen] = useState(false);
  const [infoData, setInfoData] = useState<{
    id?: number;
    indicatorName: string;
    value: string | number;
    units?: string;
    min: number | null;
    max: number | null;
    reasons: Reason[];
    date: string;
    status?: Status;
  } | null>(null);

  const openInfo = (
    cell: {
      id?: number;
      v: string | number;
      units?: string;
      min: number | null;
      max: number | null;
      reasons: Reason[];
      status?: Status;
    },
    indicatorName: string,
    dateISO: string
  ) => {
    setInfoData({
      id: cell.id,
      indicatorName,
      value: cell.v,
      units: cell.units,
      min: cell.min,
      max: cell.max,
      reasons: cell.reasons ?? [],
      date: dateISO,
      status: cell.status,
    });
    setInfoOpen(true);
  };

  const closeInfo = () => {
    setInfoOpen(false);
    setInfoData(null);
  };
  useEffect(() => {
    if (!infoOpen) return;
    const onKey = (e: KeyboardEvent) => e.key === "Escape" && closeInfo();
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [infoOpen]);

  // Модалка редактирования
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [editingMeasureId, setEditingMeasureId] = useState<number | null>(null);
  const [editValue, setEditValue] = useState<string>("");
  const [editDate, setEditDate] = useState<string>(todayISO());
  const [editUnits, setEditUnits] = useState<string>("");
  const [editName, setEditName] = useState<string>("");

  const reloadSummary = async () => {
    const arr = await loadSummary();
    setBlocks(arr);
  };

  // Первичная загрузка
  useEffect(() => {
    let alive = true;
    (async () => {
      try {
        const [person, summary, groupsArr] = await Promise.all([
          loadPerson().catch(() => null),
          loadSummary().catch(() => [] as SummaryGroup[]),
          groupsApi.getAll().catch(() => [] as IndicatorGroup[]),
        ]);
        if (!alive) return;

        setPersonName(person?.name ?? "");
        setBlocks(summary);
        setAllGroups(groupsArr);

        const opts: IndicatorOption[] = groupsArr.flatMap((g) =>
          (g?.indicators ?? []).map((it) => ({
            key: `${g.groupName}||${it.name}`,
            label: it.name,
            name: it.name,
            units: Array.isArray(it.units) ? it.units.filter(Boolean) : [],
            groupName: g.groupName,
          }))
        );
        opts.sort((a, b) => a.label.localeCompare(b.label, "ru"));
        setIndicatorOptions(opts);
      } catch (e) {
        console.error(e);
      } finally {
        if (alive) setLoading(false);
      }
    })();
    return () => {
      alive = false;
    };
  }, []);

  const existingGroupNames = useMemo(() => {
    const s = new Set<string>();
    for (const b of blocks) s.add(b.groupName);
    for (const g of extraGroups) s.add(g);
    return s;
  }, [blocks, extraGroups]);

  const missingGroups = useMemo(
    () => allGroups.filter((g) => !existingGroupNames.has(g.groupName)),
    [allGroups, existingGroupNames]
  );

  const displayGroups: SummaryGroup[] = useMemo(() => {
    const arr: SummaryGroup[] = [...blocks];
    for (const gname of extraGroups) {
      if (!arr.find((a) => a.groupName === gname)) {
        arr.push({ groupName: gname, dates: [], metas: [] });
      }
    }
    return arr;
  }, [blocks, extraGroups]);

  useEffect(() => {
    setGroupIndex((i) => Math.min(Math.max(0, i), Math.max(0, displayGroups.length - 1)));
  }, [displayGroups.length]);

  const current = displayGroups[groupIndex] || { groupName: "", dates: [], metas: [] };
  const groupDates = useMemo(() => [...(current.dates ?? [])].sort(), [current.dates]);
  const currentGroupName = current.groupName || "";

  const optionsForCurrentGroup = useMemo(
    () => indicatorOptions.filter((o) => o.groupName === currentGroupName),
    [indicatorOptions, currentGroupName]
  );

  useEffect(() => {
    if (optionsForCurrentGroup.length === 0) return;
    if (!indicatorKey || !optionsForCurrentGroup.some((o) => o.key === indicatorKey)) {
      const first = optionsForCurrentGroup[0];
      setIndicatorKey(first.key);
      setUnit(first.units[0] ?? "");
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentGroupName, optionsForCurrentGroup]);

  const valueMap = useMemo(() => {
    const m = new Map<string, {
      id?: number;
      v: string | number;
      status: Status;
      units?: string;
      name: string;
      min: number | null;
      max: number | null;
      reasons: Reason[];
    }>();
    for (const meta of current.metas ?? []) {
      for (const meas of meta.measures ?? []) {
        m.set(`${meta.indicatorName}||${meas.regDate}`, {
          id: meas.id,
          v: meas.currentValue,
          status: normStatus(meas.status),
          units: meas.units,
          name: meta.indicatorName,
          min: meas.minValue ?? null,
          max: meas.maxValue ?? null,
          reasons: Array.isArray(meas.reasons) ? meas.reasons : [],
        });
      }
    }
    return m;
  }, [current]);

  const selectedIndicator = useMemo(
    () => indicatorOptions.find((o) => o.key === indicatorKey),
    [indicatorOptions, indicatorKey]
  );

  useEffect(() => {
    if (selectedIndicator) {
      if (!selectedIndicator.units.includes(unit)) {
        setUnit(selectedIndicator.units[0] ?? "");
      }
    } else {
      setUnit("");
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedIndicator]);

  // Выбор индикатора из общего поиска (по всем группам).
  // После выбора автоматически переключаем сводную таблицу на нужную группу,
  // добавляя её во вкладки, если она ещё не отображается.
  const handlePickIndicator = (key: string) => {
    setIndicatorKey(key);
    const opt = indicatorOptions.find((o) => o.key === key);
    if (!opt) return;
    setUnit(opt.units[0] ?? "");

    const present = displayGroups.findIndex((g) => g.groupName === opt.groupName);
    if (present >= 0) {
      setGroupIndex(present);
      return;
    }
    setExtraGroups((prev) => {
      const next = prev.includes(opt.groupName) ? prev : [...prev, opt.groupName];
      const allNames = [...blocks.map((b) => b.groupName), ...next];
      const idx = allNames.findIndex((n) => n === opt.groupName);
      setTimeout(() => setGroupIndex(idx >= 0 ? idx : 0), 0);
      return next;
    });
  };

  const onSave = async (e: FormEvent) => {
    e.preventDefault();
    if (!selectedIndicator) return notify("Выберите индикатор", "error");
    if (!unit) return notify("Выберите единицы измерения", "error");
    if (!value || !date) return notify("Заполните значение и дату", "error");

    setSaving(true);
    try {
      await createMeasure({
        name: selectedIndicator.name,
        units: unit,
        currentValue: Number(value),
        regDate: isoToServer(date),
      });
      await reloadSummary();
      setValue("");
      notify("Измерение сохранено", "success");
    } catch (err: any) {
      console.error(err);
      notify(err?.response?.data?.detail || "Ошибка при сохранении измерения", "error");
    } finally {
      setSaving(false);
    }
  };

  const columnsForPage = useMemo(() => {
    return groupDates.slice(currentPage * columnsPerPage, (currentPage + 1) * columnsPerPage);
  }, [groupDates, currentPage, columnsPerPage]);

  const totalPages = Math.max(1, Math.ceil(groupDates.length / columnsPerPage));
  const goToPreviousPage = () => currentPage > 0 && setCurrentPage(currentPage - 1);
  const goToNextPage = () => currentPage < totalPages - 1 && setCurrentPage(currentPage + 1);

  // Лента групп
  const tabsRef = useRef<HTMLDivElement>(null);
  const [canScrollLeft, setCanScrollLeft] = useState(false);
  const [canScrollRight, setCanScrollRight] = useState(false);

  const updateTabsScrollState = () => {
    const el = tabsRef.current;
    if (!el) return;
    setCanScrollLeft(el.scrollLeft > 0);
    setCanScrollRight(el.scrollLeft + el.clientWidth < el.scrollWidth - 1);
  };
  const scrollTabsBy = (px: number) => {
    tabsRef.current?.scrollBy({ left: px, behavior: "smooth" });
  };
  useEffect(() => {
    const el = tabsRef.current;
    if (!el) return;
    updateTabsScrollState();
    const onScroll = () => updateTabsScrollState();
    const ro = new ResizeObserver(updateTabsScrollState);
    el.addEventListener("scroll", onScroll);
    ro.observe(el);
    return () => {
      el.removeEventListener("scroll", onScroll);
      ro.disconnect();
    };
  }, [displayGroups.length]);

  const handleDecrypt = async () => {
    if (!decryptDate) {
      notify("Выберите дату для расшифровки", "error");
      return;
    }
    setLoadingDecrypt(true);
    try {
      const data = await decryptMeasures(decryptDate);
      const dataItems: DecryptDataItem[] = Object.entries(data)
        .map(([name, val]) => ({
          id: val.reasonId,
          name,
          value: val.matchesCount,
          indicators: val.indicators ?? [],
        }))
        .sort((a, b) => b.value - a.value);

      setDecryptData({
        largeValues: dataItems.slice(0, 8),
        smallValues: dataItems.slice(8),
      });
      if (dataItems.length === 0) notify("На выбранную дату нет данных для расшифровки", "info");
    } catch (e) {
      console.error("Ошибка при расшифровке:", e);
      notify("Ошибка при загрузке расшифровки", "error");
    } finally {
      setLoadingDecrypt(false);
    }
  };

  const handleEditSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (editingMeasureId == null) return;
    if (!editName || !editUnits) {
      notify("Укажите имя индикатора и единицы измерения", "error");
      return;
    }
    try {
      await updateMeasure(editingMeasureId, {
        name: editName,
        units: editUnits,
        currentValue: Number(editValue),
        regDate: isoToServer(editDate),
      });
      setEditModalOpen(false);
      setEditingMeasureId(null);
      await reloadSummary();
      notify("Измерение обновлено", "success");
    } catch (err) {
      console.error(err);
      notify("Ошибка при редактировании измерения", "error");
    }
  };

  if (loading) {
    return (
      <div className="loading-screen">
        <div className="spinner"></div>
        <p>Загрузка...</p>
      </div>
    );
  }

  return (
    <div className="person-page">
      {personName && <h2 className="person-name-title">Анализы: {personName}</h2>}

      <div className="decrypt-section">
        <h3 className="text-grupp">Группы индикаторов:</h3>
        <div className="tabs-bar-wrap">
          {canScrollLeft && (
            <button type="button" className="chevron chevron-left" aria-label="Листать влево" onClick={() => scrollTabsBy(-240)}>➤</button>
          )}
          <div className="tabs-viewport">
            <div className="tabs-row" ref={tabsRef}>
              {missingGroups.length > 0 && (
                <button type="button" className="plus-btn" onClick={() => setShowAddGroup(true)} title="Добавить группу">+</button>
              )}
              {displayGroups.map((g, i) => (
                <button
                  key={g.groupName || i}
                  type="button"
                  className={`tab-btn ${i === groupIndex ? "active" : ""}`}
                  onClick={() => setGroupIndex(i)}
                  title={g.groupName}
                >
                  {g.groupName || `Группа ${i + 1}`}
                </button>
              ))}
            </div>
          </div>
          {canScrollRight && (
            <button type="button" className="chevron chevron-right" aria-label="Листать вправо" onClick={() => scrollTabsBy(240)}>➤</button>
          )}
        </div>
      </div>

      <div className="decrypt-section">
        <h3 className="text-grupp">Сохранить индикатор:</h3>
        <form className="form-card person-input-row" onSubmit={onSave}>
          <SearchSelect
            id="indicator"
            value={indicatorKey}
            onChange={handlePickIndicator}
            options={indicatorOptions.map((o) => ({ value: o.key, label: o.label, sub: o.groupName }))}
            placeholder="Поиск индикатора..."
          />
          <FloatingTextInput id="value" type="number" label="Значение" value={value} onChange={(e) => setValue(e.target.value)} />
          <FloatingSelect
            id="units"
            label="Единицы"
            value={unit}
            onChange={(e) => setUnit(e.target.value)}
            options={(indicatorOptions.find((o) => o.key === indicatorKey)?.units ?? []).map((u) => ({ value: u, label: u }))}
          />
          <FloatingTextInput id="date" type="date" label="Дата анализа" value={date} onChange={(e) => setDate(e.target.value)} />
          <button type="submit" disabled={saving}>{saving ? "Сохранение..." : "Сохранить"}</button>
        </form>
      </div>

      <div className="table-wrapper">
        <table className="person-grid">
          <thead>
            <tr>
              <th className="sticky-col">{current.groupName || "Индикатор"}</th>
              {columnsForPage.map((d) => (
                <th key={d}>{formatShort(d)}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {current.metas.map((meta) => (
              <tr key={meta.indicatorName}>
                <td className="sticky-col">{meta.indicatorName}</td>
                {columnsForPage.map((d) => {
                  const cell = valueMap.get(`${meta.indicatorName}||${d}`);
                  const cls = cell?.status === "ok" ? "cell-ok" : cell?.status === "raise" || cell?.status === "fall" ? "cell-raise" : "";
                  return (
                    <td
                      key={d}
                      className={cls}
                      onClick={() =>
                        cell &&
                        openInfo(
                          { id: cell.id, v: cell.v, units: cell.units, min: cell.min, max: cell.max, status: cell.status, reasons: cell.reasons },
                          meta.indicatorName,
                          d
                        )
                      }
                      title="Клик — подробная информация"
                    >
                      <span>
                        {cell?.v ?? ""}
                        {cell?.status === "raise" && " ↑"}
                        {cell?.status === "fall" && " ↓"}
                      </span>
                    </td>
                  );
                })}
              </tr>
            ))}
            {current.metas.length === 0 && (
              <tr>
                <td colSpan={1 + columnsForPage.length} className="no-data">Нет показателей в группе</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <div className="pagination-container">
        <button className="pagination-button" onClick={goToPreviousPage} disabled={currentPage === 0}>Назад</button>
        <span className="pagination-text">Страница {currentPage + 1} из {totalPages}</span>
        <button className="pagination-button" onClick={goToNextPage} disabled={currentPage === totalPages - 1}>Вперед</button>
      </div>

      <div className="decrypt-section">
        <h3 className="text-grupp">Расшифровка по дате:</h3>
        <div className="decrypt-controls">
          <input type="date" value={decryptDate} onChange={(e) => setDecryptDate(e.target.value)} />
          <button onClick={handleDecrypt} disabled={loadingDecrypt}>{loadingDecrypt ? "Загрузка..." : "Расшифровать"}</button>
        </div>
        <div className={`decrypt-chart-wrapper ${decryptData.largeValues.length || decryptData.smallValues.length ? "active" : ""}`}>
          <div className="chart-legend-left">
            {decryptData.largeValues.map((item, idx) => (
              <div
                key={item.id}
                className="legend-item"
                onMouseEnter={() => setHoveredReasonId(idx)}
                onMouseLeave={() => setHoveredReasonId(null)}
              >
                <span className="legend-color" style={{ backgroundColor: COLORS[idx % COLORS.length] }} />
                <span className="legend-name">{item.name}</span>
                {hoveredReasonId === idx && (
                  <button
                    className="exclude-button"
                    disabled={loadingExclusion}
                    onClick={async () => {
                      if (!(await confirm("Исключить эту причину из расшифровок?", { title: "Исключение причины", okText: "Исключить" }))) return;
                      setLoadingExclusion(true);
                      try {
                        await addExclusions([item.id]);
                        setDecryptData((prev) => ({
                          largeValues: prev.largeValues.filter((x) => x.id !== item.id),
                          smallValues: prev.smallValues.filter((s) => s.id !== item.id),
                        }));
                        notify("Причина исключена", "success");
                      } catch (e) {
                        console.error(e);
                        notify("Ошибка при исключении причины", "error");
                      } finally {
                        setLoadingExclusion(false);
                      }
                    }}
                  >
                    Исключить
                  </button>
                )}
              </div>
            ))}
          </div>

          <ResponsiveContainer width="30%" height={200}>
            <PieChart>
              <Pie data={decryptData.largeValues} dataKey="value" cx="50%" cy="50%" outerRadius={100} label={false}>
                {decryptData.largeValues.map((_, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip
                content={({ active, payload }) => {
                  if (active && payload && payload.length > 0) {
                    const item = payload[0].payload as DecryptDataItem;
                    return (
                      <div className="custom-tooltip">
                        <div><b>Название причины:</b> {item.name}</div>
                        <div><b>Кол-во совпадений:</b> {item.value}</div>
                        <div><b>Встречается в индикаторах:</b> {item.indicators.join(", ")}</div>
                      </div>
                    );
                  }
                  return null;
                }}
              />
            </PieChart>
          </ResponsiveContainer>

          {decryptData.smallValues.length > 0 && (
            <div className="small-values-list">
              <h4>Остальные причины:</h4>
              <ul>
                {decryptData.smallValues.map((entry) => (
                  <li key={entry.id}><span>{entry.name}: {entry.value}</span></li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </div>

      {infoOpen && infoData && (
        <>
          <div className="info-overlay" onClick={closeInfo} />
          <div className="info-card" role="dialog" aria-modal="true">
            <button aria-label="Закрыть" className="info-close" onClick={closeInfo}>×</button>
            <h3 className="info-title">Информация</h3>
            <div className="info-row"><b>Индикатор:</b> {infoData.indicatorName}</div>
            <div className="info-row"><b>Дата:</b> {formatShort(infoData.date)}</div>
            <div className="info-row"><b>Значение:</b> {infoData.value} {infoData.units}</div>
            {infoData.status && (
              <div className="info-row">
                <b>Статус:</b>{" "}
                <span className={infoData.status === "ok" ? "status-ok" : infoData.status === "raise" ? "status-raise" : "status-fall"}>
                  {infoData.status === "ok" ? "Хорошо" : infoData.status === "raise" ? "Повышено" : "Понижено"}
                </span>
              </div>
            )}
            {infoData.min != null && <div className="info-row"><b>Мин значение:</b> {infoData.min} {infoData.units}</div>}
            {infoData.max != null && <div className="info-row"><b>Макс значение:</b> {infoData.max} {infoData.units}</div>}
            {infoData.reasons?.length ? (
              <div className="info-reasons">
                <b>Причины:</b>
                <ul>{infoData.reasons.map((r) => <li key={r.id}>{r.name}</li>)}</ul>
              </div>
            ) : null}

            {infoData.id && (
              <div className="modal-actions" style={{ marginTop: 12 }}>
                <button
                  type="button"
                  onClick={() => {
                    setEditingMeasureId(infoData.id!);
                    setEditValue(String(infoData.value ?? ""));
                    setEditDate(infoData.date);
                    setEditUnits(infoData.units || "");
                    setEditName(infoData.indicatorName);
                    setEditModalOpen(true);
                    setInfoOpen(false);
                  }}
                >
                  Редактировать
                </button>
                <button
                  type="button"
                  onClick={async () => {
                    if (!(await confirm("Удалить это измерение?", { title: "Удаление измерения", okText: "Удалить", danger: true }))) return;
                    try {
                      await removeMeasure(infoData.id!);
                      await reloadSummary();
                      closeInfo();
                      notify("Измерение удалено", "success");
                    } catch (err) {
                      console.error(err);
                      notify("Ошибка при удалении измерения", "error");
                    }
                  }}
                >
                  Удалить
                </button>
              </div>
            )}
          </div>
        </>
      )}

      {showAddGroup && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3 className="modal-title">Добавить группу</h3>
            {missingGroups.length === 0 ? (
              <p>Все группы уже добавлены.</p>
            ) : (
              <div className="modal-group-list">
                {missingGroups.map((g) => (
                  <button
                    key={g.groupName}
                    onClick={() => {
                      setExtraGroups((prev) => {
                        const next = [...prev, g.groupName];
                        setShowAddGroup(false);
                        setTimeout(() => {
                          const allNames = [...blocks.map((b) => b.groupName), ...next];
                          const idx = allNames.findIndex((n) => n === g.groupName);
                          setGroupIndex(idx >= 0 ? idx : 0);
                        }, 0);
                        return next;
                      });
                    }}
                  >
                    {g.groupName}
                  </button>
                ))}
              </div>
            )}
            <div className="modal-actions">
              <button onClick={() => setShowAddGroup(false)}>Закрыть</button>
            </div>
          </div>
        </div>
      )}

      {editModalOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <form className="form-redact" onSubmit={handleEditSubmit}>
              <h3 className="modal-title">Редактировать</h3>
              <div className="modal-subtitle">Индикатор: {editName}</div>
              <FloatingTextInput id="editValue" type="number" label="Значение" value={editValue} onChange={(e) => setEditValue(e.target.value)} />
              <FloatingTextInput id="editDate" type="date" label="Дата анализа" value={editDate} onChange={(e) => setEditDate(e.target.value)} />
              <FloatingSelect
                id="editUnits"
                label="Единицы"
                value={editUnits}
                onChange={(e) => setEditUnits(e.target.value)}
                options={(indicatorOptions.find((o) => o.name === editName)?.units ?? []).map((u) => ({ value: u, label: u }))}
              />
              <div className="modal-actions" style={{ marginTop: 12 }}>
                <button type="submit">Сохранить</button>
                <button type="button" onClick={() => setEditModalOpen(false)}>Отмена</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
