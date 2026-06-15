import { useCallback, useEffect, useState } from "react";
import { actuatorApi } from "../../api/actuatorApi";
import { httpLogsApi } from "../../api/httpLogsApi";
import type {
  Health,
  HttpLogDetail,
  HttpLogSummary,
  LoggersResponse,
  Page,
} from "../../api/types";
import "../../styles/admin.css";

type Tab = "status" | "http" | "loggers" | "logfile";

const TABS: { key: Tab; label: string }[] = [
  { key: "status", label: "Состояние" },
  { key: "http", label: "HTTP-логи" },
  { key: "loggers", label: "Логгеры" },
  { key: "logfile", label: "Файл логов" },
];

const statusClass = (s: number) =>
  s >= 500 ? "http-5xx" : s >= 400 ? "http-4xx" : s >= 300 ? "http-3xx" : "http-2xx";

const fmtTime = (iso: string) => {
  try {
    return new Date(iso).toLocaleString("ru-RU");
  } catch {
    return iso;
  }
};

const pretty = (body: string | null) => {
  if (!body) return "—";
  try {
    return JSON.stringify(JSON.parse(body), null, 2);
  } catch {
    return body;
  }
};

export default function System() {
  const [tab, setTab] = useState<Tab>("status");

  return (
    <div className="admin">
      <div className="admin-header">
        <div className="admin-title">Технический мониторинг</div>
      </div>

      <div className="sys-tabs">
        {TABS.map((t) => (
          <button
            key={t.key}
            className={`sys-tab ${tab === t.key ? "active" : ""}`}
            onClick={() => setTab(t.key)}
          >
            {t.label}
          </button>
        ))}
      </div>

      {tab === "status" && <StatusTab />}
      {tab === "http" && <HttpTab />}
      {tab === "loggers" && <LoggersTab />}
      {tab === "logfile" && <LogfileTab />}
    </div>
  );
}

function StatusTab() {
  const [health, setHealth] = useState<Health | null>(null);
  const [memUsed, setMemUsed] = useState<number | null>(null);
  const [memMax, setMemMax] = useState<number | null>(null);
  const [cpu, setCpu] = useState<number | null>(null);
  const [uptime, setUptime] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const [h, used, max, c, up] = await Promise.all([
        actuatorApi.health(),
        actuatorApi.metric("jvm.memory.used"),
        actuatorApi.metric("jvm.memory.max"),
        actuatorApi.metric("system.cpu.usage"),
        actuatorApi.metric("process.uptime"),
      ]);
      setHealth(h);
      setMemUsed(used);
      setMemMax(max);
      setCpu(c);
      setUptime(up);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const mb = (v: number | null) => (v == null ? "—" : `${Math.round(v / 1024 / 1024)} МБ`);
  const fmtUptime = (s: number | null) => {
    if (s == null) return "—";
    const d = Math.floor(s / 86400);
    const h = Math.floor((s % 86400) / 3600);
    const m = Math.floor((s % 3600) / 60);
    return `${d > 0 ? d + "д " : ""}${h}ч ${m}м`;
  };

  const portainerUrl = `https://${window.location.hostname}:9443`;

  return (
    <>
      <div className="sys-toolbar">
        <button className="admin-btn admin-btn--ghost" onClick={load}>Обновить</button>
        <div className="spacer" />
        <a className="admin-btn" href={portainerUrl} target="_blank" rel="noreferrer">
          Контейнеры (Portainer) ↗
        </a>
      </div>

      {loading ? (
        <div className="admin-loading">Загрузка...</div>
      ) : (
        <>
          <div className="sys-cards">
            <div className="sys-card">
              <h4>Сервис</h4>
              <div className={`big ${health?.status === "UP" ? "sys-status-up" : "sys-status-down"}`}>
                {health?.status ?? "—"}
              </div>
            </div>
            <div className="sys-card">
              <h4>Память (JVM)</h4>
              <div className="big">{mb(memUsed)}</div>
              <div className="sub">из {mb(memMax)}</div>
            </div>
            <div className="sys-card">
              <h4>CPU системы</h4>
              <div className="big">{cpu == null ? "—" : `${(cpu * 100).toFixed(1)}%`}</div>
            </div>
            <div className="sys-card">
              <h4>Аптайм</h4>
              <div className="big">{fmtUptime(uptime)}</div>
            </div>
          </div>

          {health?.components && (
            <div className="admin-table-wrap">
              <table className="admin-table">
                <thead>
                  <tr>
                    <th>Компонент</th>
                    <th>Статус</th>
                    <th>Детали</th>
                  </tr>
                </thead>
                <tbody>
                  {Object.entries(health.components).map(([name, c]) => (
                    <tr key={name}>
                      <td>{name}</td>
                      <td>
                        <span className={c.status === "UP" ? "sys-status-up" : "sys-status-down"}>
                          {c.status}
                        </span>
                      </td>
                      <td className="admin-muted">
                        {c.details ? JSON.stringify(c.details) : "—"}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </>
      )}
    </>
  );
}

function HttpTab() {
  const SIZE = 50;
  const [page, setPage] = useState(0);
  const [onlyErrors, setOnlyErrors] = useState(false);
  const [data, setData] = useState<Page<HttpLogSummary> | null>(null);
  const [loading, setLoading] = useState(true);
  const [detail, setDetail] = useState<HttpLogDetail | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      setData(await httpLogsApi.list(page, SIZE, onlyErrors));
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  }, [page, onlyErrors]);

  useEffect(() => {
    load();
  }, [load]);

  const clear = async () => {
    if (!window.confirm("Очистить весь HTTP-лог?")) return;
    await httpLogsApi.clear();
    setPage(0);
    load();
  };

  const openDetail = async (id: number) => {
    try {
      setDetail(await httpLogsApi.get(id));
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <>
      <div className="sys-toolbar">
        <label className="sys-checkbox">
          <input
            type="checkbox"
            checked={onlyErrors}
            onChange={(e) => {
              setOnlyErrors(e.target.checked);
              setPage(0);
            }}
          />
          Только ошибки (5xx)
        </label>
        <button className="admin-btn admin-btn--ghost" onClick={load}>Обновить</button>
        <div className="spacer" />
        <button className="admin-btn admin-btn--danger" onClick={clear}>Очистить лог</button>
      </div>

      {loading ? (
        <div className="admin-loading">Загрузка...</div>
      ) : (
        <>
          <div className="admin-table-wrap">
            <table className="admin-table admin-table--wide">
              <thead>
                <tr>
                  <th style={{ width: 160 }}>Время</th>
                  <th style={{ width: 70 }}>Метод</th>
                  <th>Путь</th>
                  <th style={{ width: 70 }}>Статус</th>
                  <th style={{ width: 90 }}>Длит.</th>
                </tr>
              </thead>
              <tbody>
                {data?.content.map((l) => (
                  <tr
                    key={l.id}
                    className={`clickable ${l.error ? "row-error" : ""}`}
                    onClick={() => openDetail(l.id)}
                  >
                    <td className="admin-muted">{fmtTime(l.createdAt)}</td>
                    <td><span className="method-pill">{l.method}</span></td>
                    <td>{l.uri}</td>
                    <td><span className={statusClass(l.status)}>{l.status}</span></td>
                    <td className="admin-muted">{l.durationMs} мс</td>
                  </tr>
                ))}
                {data && data.content.length === 0 && (
                  <tr><td colSpan={5} className="admin-empty">Записей нет</td></tr>
                )}
              </tbody>
            </table>
          </div>

          {data && data.totalPages > 1 && (
            <div className="sys-pagination">
              <button
                className="admin-btn admin-btn--ghost"
                disabled={page <= 0}
                onClick={() => setPage((p) => p - 1)}
              >
                ← Назад
              </button>
              <span>{page + 1} / {data.totalPages}</span>
              <button
                className="admin-btn admin-btn--ghost"
                disabled={page >= data.totalPages - 1}
                onClick={() => setPage((p) => p + 1)}
              >
                Вперёд →
              </button>
            </div>
          )}
        </>
      )}

      {detail && (
        <div className="admin-modal-backdrop" onClick={() => setDetail(null)}>
          <div className="admin-modal" onClick={(e) => e.stopPropagation()}>
            <h3>
              <span className="method-pill">{detail.method}</span>{" "}
              <span className={statusClass(detail.status)}>{detail.status}</span>{" "}
              {detail.uri}
            </h3>

            <div className="admin-muted">
              {fmtTime(detail.createdAt)} · {detail.durationMs} мс · IP {detail.clientIp ?? "—"}
              {detail.principal ? ` · ${detail.principal}` : ""}
            </div>

            {detail.queryString && (
              <>
                <div className="sys-detail-label">Query</div>
                <pre className="sys-pre">{detail.queryString}</pre>
              </>
            )}

            <div className="sys-detail-label">Тело запроса</div>
            <pre className="sys-pre">{pretty(detail.requestBody)}</pre>

            <div className="sys-detail-label">Тело ответа</div>
            <pre className="sys-pre">{pretty(detail.responseBody)}</pre>

            <div className="admin-modal-actions">
              <button className="admin-btn admin-btn--ghost" onClick={() => setDetail(null)}>
                Закрыть
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

function LoggersTab() {
  const [data, setData] = useState<LoggersResponse | null>(null);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState<string | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      setData(await actuatorApi.loggers());
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const change = async (name: string, level: string) => {
    setBusy(name);
    try {
      await actuatorApi.setLevel(name, level || null);
      await load();
    } catch (e) {
      console.error(e);
      alert("Не удалось сменить уровень");
    } finally {
      setBusy(null);
    }
  };

  const entries = data
    ? Object.entries(data.loggers)
        .filter(([name]) => name.toLowerCase().includes(search.toLowerCase()))
        .sort(([a], [b]) => a.localeCompare(b))
    : [];

  return (
    <>
      <div className="sys-toolbar">
        <input
          className="admin-search"
          placeholder="Поиск логгера..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button className="admin-btn admin-btn--ghost" onClick={load}>Обновить</button>
      </div>

      {loading ? (
        <div className="admin-loading">Загрузка...</div>
      ) : (
        <div className="admin-table-wrap">
          <table className="admin-table admin-table--wide">
            <thead>
              <tr>
                <th>Логгер</th>
                <th style={{ width: 110 }}>Эффект.</th>
                <th style={{ width: 150 }}>Уровень</th>
              </tr>
            </thead>
            <tbody>
              {entries.slice(0, 300).map(([name, l]) => (
                <tr key={name}>
                  <td style={{ wordBreak: "break-all" }}>{name === "ROOT" ? "ROOT" : name}</td>
                  <td><span className={`level-pill level-${l.effectiveLevel}`}>{l.effectiveLevel}</span></td>
                  <td>
                    <select
                      className="admin-search"
                      style={{ minWidth: 110 }}
                      value={l.configuredLevel ?? ""}
                      disabled={busy === name}
                      onChange={(e) => change(name, e.target.value)}
                    >
                      <option value="">(наследуется)</option>
                      {data!.levels.map((lvl) => (
                        <option key={lvl} value={lvl}>{lvl}</option>
                      ))}
                    </select>
                  </td>
                </tr>
              ))}
              {entries.length === 0 && (
                <tr><td colSpan={3} className="admin-empty">Ничего не найдено</td></tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
}

function LogfileTab() {
  const [text, setText] = useState("");
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const full = await actuatorApi.logfile();
      const lines = full.split("\n");
      setText(lines.slice(-500).join("\n"));
    } catch (e) {
      console.error(e);
      setText("Файл логов недоступен.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  return (
    <>
      <div className="sys-toolbar">
        <span className="admin-muted">Последние 500 строк</span>
        <button className="admin-btn admin-btn--ghost" onClick={load}>Обновить</button>
      </div>
      {loading ? (
        <div className="admin-loading">Загрузка...</div>
      ) : (
        <pre className="sys-pre">{text}</pre>
      )}
    </>
  );
}
