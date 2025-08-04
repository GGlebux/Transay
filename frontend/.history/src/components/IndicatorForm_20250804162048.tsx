import { useState, useEffect } from 'react';

const genders = ['male', 'female', 'both'];

function IndicatorForm() {
  const [units, setUnits] = useState<string[]>([]);

  useEffect(() => {
    // Заменить на свой API
    setUnits(['mg/L', 'mmol/L', 'g/dL']);
  }, []);

  return (
    <form className="form-card">
      <h2>Индикатор</h2>
      <input placeholder="Англ название" />
      <input placeholder="Рус название" />

      <select>
        <option>Выбери единицу измерения</option>
        {units.map((u) => (
          <option key={u}>{u}</option>
        ))}
      </select>

      <select>
        {genders.map((g) => (
          <option key={g} value={g}>
            {g}
          </option>
        ))}
      </select>

      <label className="checkbox">
        <input type="checkbox" />
        Беременна
      </label>

      <label>Минимальный возраст</label>
      <div className="age-row">
        <input placeholder="Годы" type="number" />
        <input placeholder="Месяцы" type="number" />
        <input placeholder="Дни" type="number" />
      </div>

      <label>Максимальный возраст</label>
      <div className="age-row">
        <input placeholder="Годы" type="number" />
        <input placeholder="Месяцы" type="number" />
        <input placeholder="Дни" type="number" />
      </div>

      <input placeholder="Минимальное значение (0.43 и т.д.)" type="number" step="0.0001" />
      <input placeholder="Максимальное значение (0.43 и т.д.)" type="number" step="0.0001" />

      <button type="submit">Отправить</button>
    </form>
  );
}

export default IndicatorForm;
