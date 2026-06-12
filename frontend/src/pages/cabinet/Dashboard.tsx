import { useEffect, useState } from "react";
import { useAuth } from "../../authe/AuthContext";
import { peopleApi } from "../../api/peopleApi";
import type { PersonPayload } from "../../api/peopleApi";
import "../../styles/dashboard.css";
import { authApi } from "../../api/authApi";
import { useNavigate } from "react-router-dom";


type FormState = {
  name: string;
  gender: "male" | "female";
  dateOfBirth: string; // формат для input: yyyy-MM-dd
  isGravid: boolean;

};

type TabType = "profile" | "security";



const toServerDate = (value: string) => {
  // yyyy-MM-dd -> dd.MM.yyyy
  if (!value || !value.includes("-")) return "";
  const [year, month, day] = value.split("-");
  if (!year || !month || !day) return "";
  return `${day}.${month}.${year}`;
};

const toInputDate = (value: string) => {
  if (!value) return "";
  if (value.includes("-")) return value; // уже правильный формат
  if (value.includes(".")) {
    const [day, month, year] = value.split(".");
    return `${year}-${month}-${day}`;
  }
  return "";
};
export default function Dashboard() {
  const { customer, refreshCustomer } = useAuth();

  const [form, setForm] = useState<FormState>({
    name: "",
    gender: "male",
    dateOfBirth: "",
    isGravid: false,
  });

  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await authApi.logout();
    } finally {
      localStorage.removeItem("accessToken");
      navigate("/login");
    }
  };

  const [initialForm, setInitialForm] = useState<FormState | null>(null);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [activeTab, setActiveTab] = useState<TabType>("profile");

  useEffect(() => {
    if (!customer?.personId) return;
    loadPerson();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [customer?.personId]);

  const loadPerson = async () => {
    try {
      const data = await peopleApi.getMe();

      const mapped: FormState = {
        name: data.name ?? "",
        gender: data.gender === "FEMALE" ? "female" : "male",
        dateOfBirth: toInputDate(data.dateOfBirth),
        // У бэка нет isGravid — состояние едет в condition (GRAVID/MENSES/BASE)
        isGravid: data.condition === "GRAVID",
      };

      setForm(mapped);
      setInitialForm(mapped);
    } catch (e) {
      console.error("Ошибка загрузки профиля:", e);
    }
  };

  const handleChange = <K extends keyof FormState>(key: K, value: FormState[K]) => {
    setForm((prev) => ({ ...prev, [key]: value }));
    if (error) setError("");
    if (success) setSuccess("");
  };

  const validate = (): string | null => {
    if (!form.name.trim()) return "Введите имя";
    if (form.name.trim().length < 2) return "Имя слишком короткое";
    if (!form.dateOfBirth) return "Введите дату рождения";
    return null;
  };

  const handleSubmit = async () => {
    const msg = validate();
    if (msg) {
      setError(msg);
      return;
    }

    const serverDate = toServerDate(form.dateOfBirth);
    if (!serverDate) {
      setError("Некорректная дата рождения");
      return;
    }

    try {
      setLoading(true);
      setError("");
      setSuccess("");

      const condition =
        form.gender === "male"
          ? "BASE"
          : form.isGravid
            ? "GRAVID"
            : "MENSES";

      const payload: PersonPayload = {
        name: form.name.trim(),
        gender: form.gender === "male" ? "MALE" : "FEMALE",
        dateOfBirth: serverDate,
        condition
      };

      if (!customer?.personId) {
        await peopleApi.create(payload);
        await refreshCustomer();
        setSuccess("Профиль успешно создан");
      } else {
        await peopleApi.update(payload);
        setSuccess("Данные успешно сохранены");
      }

      setInitialForm(form);
    } catch (e) {
      console.error(e);
      setError("Ошибка сохранения. Попробуйте позже.");
    } finally {
      setLoading(false);
    }
  };

  const hasChanges =
    initialForm === null
      ? form.name !== "" || form.dateOfBirth !== "" || form.gender !== "male" || form.isGravid !== false
      : JSON.stringify(form) !== JSON.stringify(initialForm);

  if (!customer) return null;

  const renderSecurity = () => {
  return (
    <div className="securityBox">

      <div className="securityInBox">
      <div className="field">
        <label>Почта</label>
        <input
          className="dashInput"
          value={customer.email}
          disabled
        />
      </div>

      <div className="field">
        <label>Текущий пароль</label>
        <input
          type="password"
          className="dashInput"
          placeholder="Введите текущий пароль"
        />
      </div>

      <div className="field">
        <label>Новый пароль</label>
        <input
          type="password"
          className="dashInput"
          placeholder="Введите новый пароль"
        />
      </div>
      </div>

      <button className="btnPrimary" style={{ marginTop: 20 }}>
        Обновить пароль
      </button>
    </div>
  );
};

  const renderProfile = () => (
  <>
    <div className="genderToggle">
            <button
              type="button"
              className={`genderBtn ${form.gender === "male" ? "active" : ""}`}
              onClick={() => {
                handleChange("gender", "male");
                handleChange("isGravid", false);
              }}
            >
              Мужчина
            </button>

            <button
              type="button"
              className={`genderBtn ${form.gender === "female" ? "active" : ""}`}
              onClick={() => handleChange("gender", "female")}
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
                onChange={(e) => handleChange("name", e.target.value)}
                placeholder="Введите имя"
              />
            </div>

            <div className="field">
              <label>Почта</label>
              <input
                className="dashInput"
                value={customer.email}
                disabled
              />
            </div>

            <div className="field">
              <label>Дата рождения</label>
              <input
                type="date"
                className="dashInput"
                value={form.dateOfBirth || ""}
                onChange={(e) => handleChange("dateOfBirth", e.target.value)}
              />
            </div>

            {form.gender === "female" && (
              <div className="field">
                <label>Беременность</label>
                <div className="genderToggle">
                  <button
                    type="button"
                    className={`genderBtn ${form.isGravid ? "active" : ""}`}
                    onClick={() => handleChange("isGravid", true)}
                  >
                    Да
                  </button>

                  <button
                    type="button"
                    className={`genderBtn ${!form.isGravid ? "active" : ""}`}
                    onClick={() => handleChange("isGravid", false)}
                  >
                    Нет
                  </button>
                </div>
              </div>
            )}
          </div>

          {error && <div className="errorBox">{error}</div>}
          {success && <div className="successBox">{success}</div>}

          <div className="btnRow">
            <button
              className="btnSecondary"
              disabled={!hasChanges || loading}
              onClick={() => initialForm && setForm(initialForm)}
            >
              Отменить изменения
            </button>

            <button
              className="btnPrimary"
              onClick={handleSubmit}
              disabled={loading || !hasChanges}
            >
              {loading
                ? "Сохранение..."
                : customer.personId
                  ? "Сохранить изменения"
                  : "Создать профиль"}
            </button>
          </div>
  </>
);



  return (
    <div className="dashboardPage">
      <div className="dashboardCard">
        <div className="dashboardLeft">
          <div className="avatar">
            <img src="https://i.pravatar.cc/200" alt="Avatar" />
          </div>

          <h2>{form.name || "Ваш профиль"}</h2>
          <p className="roleText">{customer.role}</p>

          <div className="sideMenu">
            <div
              className={`menuItem ${activeTab === "profile" ? "active" : ""}`}
              onClick={() => setActiveTab("profile")}
            >
            Перс. информация
            </div>

            <div
              className={`menuItem ${activeTab === "security" ? "active" : ""}`}
              onClick={() => setActiveTab("security")}
            >
            Логин & Пароль
            </div>

            <div className="menuItem logout" onClick={handleLogout}>
              Выход
            </div>
          </div>
        </div>

        <div className="dashboardRight">
  {activeTab === "profile" && (
    <>
      <h1 className="dashTitle">Персональная информация</h1>
      {renderProfile()}
    </>
  )}

  {activeTab === "security" && (
    <>
      <h1 className="dashTitle">Логин & Пароль</h1>
      {renderSecurity()}
    </>
  )}
</div>
      </div>
    </div>
  );
}