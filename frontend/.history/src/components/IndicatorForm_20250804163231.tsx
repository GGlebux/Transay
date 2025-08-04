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

    <label className="form-label">
      Англ название
      <input type="text" />
    </label>

    <label className="form-label">
      Рус название
      <input type="text" />
    </label>

    <label className="form-label">
      Единицы измерения
      <select>
        <option>Выбери единицу измерения</option>
        {units.map((u) => (
          <option key={u}>{u}</option>
        ))}
      </select>
    </label>

    <label className="form-label">
      Гендер
      <select>
        {genders.map((g) => (
          <option key={g} value={g}>
            {g}
          </option>
        ))}
      </select>
    </label>

    <label className="form-label checkbox">
      <input type="checkbox" />
      Беременна
    </label>

    <label className="form-label">Минимальный возраст</label>
    <div className="age-row">
      <label className="form-label">Годы<input type="number" /></label>
      <label className="form-label">Месяцы<input type="number" /></label>
      <label className="form-label">Дни<input type="number" /></label>
    </div>

    <label className="form-label">Максимальный возраст</label>
    <div className="age-row">
      <label className="form-label">Годы<input type="number" /></label>
      <label className="form-label">Месяцы<input type="number" /></label>
      <label className="form-label">Дни<input type="number" /></label>
    </div>

    <label className="form-label">
      Минимальное значение
      <input type="number" step="0.0001" />
    </label>

    <label className="form-label">
      Максимальное значение
      <input type="number" step="0.0001" />
    </label>

    <button type="submit">Отправить</button>
  </form>
);


export default IndicatorForm;
