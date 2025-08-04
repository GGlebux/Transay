import { useEffect, useState} from 'react';
import axios from 'axios';

type Units = string[];

const genders = ['male', 'female', 'both'];

function IndicatorForm() {
  const [units, setUnits] = useState<Units>([]);
  const [engName, setEngName] = useState<string>('');
  const [rusName, setRusName] = useState<string>('');
  const [gender, setGender] = useState<string>('male');
  const [gravid, setGravid] = useState<boolean>(false);
  const [minAge, setMinAge] = useState({ years: 0, months: 0, days: 0 });
  const [maxAge, setMaxAge] = useState({ years: 0, months: 0, days: 0 });
  const [minValue, setMinValue] = useState<number | ''>('');
  const [maxValue, setMaxValue] = useState<number | ''>('');
  const [unit, setUnit] = useState<string>('');

  useEffect(() => {
    axios
      .get<Units>('http://localhost:8080/indicators/units')
      .then((res) => setUnits(res.data.sort()))
      .catch((err) => console.error('Ошибка при получении units:', err));
  }, []);

  const onSubmit = (e: FormEvent) => {
    e.preventDefault();
    axios
      .post('http://localhost:8080/indicators', {
        id: Date.now(),
        engName,
        rusName,
        gender,
        minAge,
        maxAge,
        minValue: Number(minValue),
        maxValue: Number(maxValue),
        units: unit,
        gravid,
      })
      .then(() => alert('Indicator sent'))
      .catch((err) => console.error('Ошибка отправки indicator:', err));
  };

  return (
    <form className="form-card" onSubmit={onSubmit}>
      <h2>Индикатор</h2>

      <label className="form-label">
        Англ название
        <input type="text" value={engName} onChange={(e) => setEngName(e.target.value)} required />
      </label>

      <label className="form-label">
        Рус название
        <input type="text" value={rusName} onChange={(e) => setRusName(e.target.value)} required />
      </label>

      <label className="form-label">
        Единицы измерения
        <select value={unit} onChange={(e) => setUnit(e.target.value)} required>
          <option value="">Выберите единицу</option>
          {units.map((u) => (
            <option key={u} value={u}>
              {u}
            </option>
          ))}
        </select>
      </label>

      <label className="form-label">
        Гендер
        <select value={gender} onChange={(e) => setGender(e.target.value)}>
          {genders.map((g) => (
            <option key={g} value={g}>
              {g}
            </option>
          ))}
        </select>
      </label>

      <label className="form-label checkbox">
        <input type="checkbox" checked={gravid} onChange={(e) => setGravid(e.target.checked)} />
        Беременна
      </label>

      <label className="form-label">Минимальный возраст</label>
      <div className="age-row">
        <label className="form-label">Годы<input type="number" value={minAge.years} onChange={(e) => setMinAge({ ...minAge, years: +e.target.value })} /></label>
        <label className="form-label">Месяцы<input type="number" value={minAge.months} onChange={(e) => setMinAge({ ...minAge, months: +e.target.value })} /></label>
        <label className="form-label">Дни<input type="number" value={minAge.days} onChange={(e) => setMinAge({ ...minAge, days: +e.target.value })} /></label>
      </div>

      <label className="form-label">Максимальный возраст</label>
      <div className="age-row">
        <label className="form-label">Годы<input type="number" value={maxAge.years} onChange={(e) => setMaxAge({ ...maxAge, years: +e.target.value })} /></label>
        <label className="form-label">Месяцы<input type="number" value={maxAge.months} onChange={(e) => setMaxAge({ ...maxAge, months: +e.target.value })} /></label>
        <label className="form-label">Дни<input type="number" value={maxAge.days} onChange={(e) => setMaxAge({ ...maxAge, days: +e.target.value })} /></label>
      </div>

      <label className="form-label">
        Минимальное значение
        <input type="number" step="0.0001" value={minValue} onChange={(e) => setMinValue(e.target.value)} required />
      </label>

      <label className="form-label">
        Максимальное значение
        <input type="number" step="0.0001" value={maxValue} onChange={(e) => setMaxValue(e.target.value)} required />
      </label>

      <button type="submit">Отправить</button>
    </form>
  );
}

export default IndicatorForm;
