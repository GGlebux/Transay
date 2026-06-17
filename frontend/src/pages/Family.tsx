import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { peopleApi } from "../api/peopleApi";
import type { Person, PersonPayload } from "../api/types";
import { isoToServer, toInputDate } from "../utils/date";
import "../styles/dashboard.css";
import "../styles/family.css";

type FormState = {
  name: string;
  gender: "male" | "female";
  dateOfBirth: string; // yyyy-MM-dd для input
  isGravid: boolean;
};

const emptyForm: FormState = {
  name: "",
  gender: "male",
  dateOfBirth: "",
  isGravid: false,
};

const calcAge = (iso: string): string => {
  if (!iso) return "";
  const b = new Date(iso);
  if (isNaN(b.getTime())) return "";
  const t = new Date();
  let age = t.getFullYear() - b.getFullYear();
  const m = t.getMonth() - b.getMonth();
  if (m < 0 || (m === 0 && t.getDate() < b.getDate())) age--;
  return `${age} лет`;
};

const personToForm = (p: Person): FormState => ({
  name: p.name ?? "",
  gender: p.gender === "FEMALE" ? "female" : "male",
  dateOfBirth: toInputDate(p.dateOfBirth),
  isGravid: p.condition === "GRAVID",
});

export default function Family() {
  const navigate = useNavigate();

  const [members, setMembers] = useState<Person[]>([]);
  const [loading, setLoading] = useState(true);

  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [form, setForm] = useState<FormState>(emptyForm);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      setMembers(await peopleApi.getFamily());
    } catch (e) {
      console.error(e);
      alert("Ошибка загрузки списка семьи");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const openCreate = () => {
    setEditingId(null);
    setForm(emptyForm);
    setError("");
    setModalOpen(true);
  };

  const openEdit = (p: Person) => {
    setEditingId(p.id);
    setForm(personToForm(p));
    setError("");
    setModalOpen(true);
  };

  const change = <K extends keyof FormState>(key: K, value: FormState[K]) => {
    setForm((prev) => ({ ...prev, [key]: value }));
    if (error) setError("");
  };

  const submit = async () => {
    if (!form.name.trim() || form.name.trim().length < 2) {
      setError("Введите имя (минимум 2 символа)");
      return;
    }
    const serverDate = isoToServer(form.dateOfBirth);
    if (!serverDate) {
      setError("Введите корректную дату рождения");
      return;
    }

    const condition =
      form.gender === "male" ? "BASE" : form.isGravid ? "GRAVID" : "MENSES";

    const payload: PersonPayload = {
      name: form.name.trim(),
      gender: form.gender === "male" ? "MALE" : "FEMALE",
      dateOfBirth: serverDate,
      condition,
    };

    setSaving(true);
    try {
      if (editingId == null) await peopleApi.createFamily(payload);
      else await peopleApi.updateFamily(editingId, payload);
      setModalOpen(false);
      await load();
    } catch (e: any) {
      setError(e?.response?.data?.detail || "Ошибка сохранения");
    } finally {
      setSaving(false);
    }
  };

  const remove = async (p: Person) => {
    if (!window.confirm(`Удалить «${p.name}» и все его анализы?`)) return;
    try {
      await peopleApi.removeFamily(p.id);
      setMembers((prev) => prev.filter((x) => x.id !== p.id));
    } catch (e) {
      console.error(e);
      alert("Ошибка при удалении");
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
    <div className="family-page">
      <div className="family-head">
        <h1>Семья</h1>
        <button className="family-add-btn" onClick={openCreate}>
          Добавить +
        </button>
      </div>

      {members.length === 0 ? (
        <div className="family-empty">
          Пока никого нет. Добавьте члена семьи, чтобы вести его анализы с учётом
          его пола и возраста.
        </div>
      ) : (
        <div className="family-grid">
          {members.map((p) => (
            <div className="family-card" key={p.id}>
              <div
                className="family-card__top"
                onClick={() => navigate(`/family/${p.id}`)}
                title="Открыть анализы"
              >
                <div className="family-avatar" aria-hidden="true">
                  {(p.name || "?").trim().charAt(0).toUpperCase()}
                </div>
                <div>
                  <div className="family-card__name">{p.name}</div>
                  <div className="family-card__meta">
                    {p.gender === "FEMALE" ? "Женщина" : "Мужчина"}
                    {calcAge(p.dateOfBirth) ? ` · ${calcAge(p.dateOfBirth)}` : ""}
                  </div>
                </div>
              </div>

              <div className="family-card__actions">
                <button
                  className="family-open-btn"
                  onClick={() => navigate(`/family/${p.id}`)}
                >
                  Анализы
                </button>
                <button className="family-edit-btn" onClick={() => openEdit(p)}>
                  Изменить
                </button>
                <button className="family-del-btn" onClick={() => remove(p)}>
                  Удалить
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {modalOpen && (
        <div className="family-modal-backdrop" onClick={() => setModalOpen(false)}>
          <div className="family-modal" onClick={(e) => e.stopPropagation()}>
            <h3>{editingId == null ? "Добавить человека" : "Редактировать"}</h3>

            <div className="genderToggle">
              <button
                type="button"
                className={`genderBtn ${form.gender === "male" ? "active" : ""}`}
                onClick={() => {
                  change("gender", "male");
                  change("isGravid", false);
                }}
              >
                Мужчина
              </button>
              <button
                type="button"
                className={`genderBtn ${form.gender === "female" ? "active" : ""}`}
                onClick={() => change("gender", "female")}
              >
                Женщина
              </button>
            </div>

            <div className="grid2">
              <div className="field">
                <label>Имя</label>
                <input
                  className="dashInput"
                  value={form.name}
                  onChange={(e) => change("name", e.target.value)}
                  placeholder="Введите имя"
                />
              </div>

              <div className="field">
                <label>Дата рождения</label>
                <input
                  type="date"
                  className="dashInput"
                  value={form.dateOfBirth}
                  onChange={(e) => change("dateOfBirth", e.target.value)}
                />
              </div>
            </div>

            {form.gender === "female" && (
              <div className="field" style={{ marginTop: 12 }}>
                <label>Беременность</label>
                <div className="genderToggle" style={{ marginBottom: 0 }}>
                  <button
                    type="button"
                    className={`genderBtn ${form.isGravid ? "active" : ""}`}
                    onClick={() => change("isGravid", true)}
                  >
                    Да
                  </button>
                  <button
                    type="button"
                    className={`genderBtn ${!form.isGravid ? "active" : ""}`}
                    onClick={() => change("isGravid", false)}
                  >
                    Нет
                  </button>
                </div>
              </div>
            )}

            {error && <div className="errorBox">{error}</div>}

            <div className="family-modal-actions">
              <button
                className="btnSecondary"
                onClick={() => setModalOpen(false)}
                disabled={saving}
              >
                Отмена
              </button>
              <button className="btnPrimary" onClick={submit} disabled={saving}>
                {saving ? "Сохранение..." : "Сохранить"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
