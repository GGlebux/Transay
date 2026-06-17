import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import axios from "axios";
import { FloatingTextInput, FloatingSelect } from "../Trans_Indicat/FloatingTextField";
import { API } from "../../apiConfig";
import { getApiErrorMessage } from "../../utils/errors";
import { MultiSelectWithSearch } from "../Trans_Indicat/MultiSelectWithSearch";

export default function TranscriptForm({
  engName,
  setEngName,
}: {
  engName: string;
  setEngName: (v: string) => void;
}) {
  const [allReasons, setAllReasons] = useState<{ id: number; name: string }[]>(
    []
  );
  const [raiseReasons, setRaiseReasons] = useState<
    { id: number; name: string }[]
  >([]);
  const [lowerReasons, setLowerReasons] = useState<
    { id: number; name: string }[]
  >([]);
  const [gender, setGender] = useState("");
  const [loading, setLoading] = useState(false); // 🔹 состояние загрузки

  useEffect(() => {
    axios
      .get<{ id: number; name: string }[]>(API.REASONS)
      .then((res) =>
        setAllReasons(res.data.sort((a, b) => a.name.localeCompare(b.name)))
      )
      .catch(console.error);
  }, []);



  const resetForm = () => {
    setEngName("");
    setGender("");
    setRaiseReasons([]);
    setLowerReasons([]);
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();

    if (
      !engName.trim() ||
      !gender.trim()) {
      alert("Заполните все обязательные поля!");
      return;
    }

    setLoading(true); // 🔹 начинаем загрузку

    try {
      await axios.post(API.TRANSCRIPTS, {
        name: engName,
        gender,
        fallsIds: lowerReasons.map((r) => r.id),
        raisesIds: raiseReasons.map((r) => r.id),
      });
      alert("Данные успешно отправлены!");
    } catch (err) {
      console.error(err);
      alert(getApiErrorMessage(err, "Ошибка при отправке данных"));
    } finally {
      setLoading(false); // 🔹 заканчиваем загрузку
    }
  };

  return (
    <form className="form-card" onSubmit={onSubmit}>
      <h2>Транскрипция</h2>
      <FloatingTextInput
        id="trans-name"
        label="Англ название"
        value={engName}
        onChange={(e) => {
          const filteredValue = e.target.value.replace(/[а-яёА-ЯЁ]/g, "");
          setEngName(filteredValue);
        }}
      />

      <FloatingSelect
        id="gender"
        label="Гендер"
        value={gender}
        onChange={(e) => setGender(e.target.value)}
        options={[
          { value: "male", label: "male" },
          { value: "female", label: "female" },
          { value: "both", label: "both" },
        ]}
      />

      <label className="multi-select__label">Причины понижения</label>
      <MultiSelectWithSearch
        options={allReasons}
        selected={lowerReasons.map((r) => r.id)}
        onChange={(ids) => {
          setLowerReasons(allReasons.filter((r) => ids.includes(r.id)));
        }}
        placeholder="Поиск причины..."
      />

      <label className="multi-select__label">Причины повышения</label>
      <MultiSelectWithSearch
        options={allReasons}
        selected={raiseReasons.map((r) => r.id)}
        onChange={(ids) => {
          setRaiseReasons(allReasons.filter((r) => ids.includes(r.id)));
        }}
        placeholder="Поиск причины..."
      />

      <div className="btn-container">
        <button
          type="button"
          className="btn-clear"
          onClick={resetForm}
          disabled={loading} // 🔹 блокировка при загрузке
        >
          Очистить
        </button>
        <button type="submit" disabled={loading}>
          {loading ? "Отправка..." : "Отправить"}
        </button>
      </div>
    </form>
  );
}
