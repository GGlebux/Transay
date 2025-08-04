import { useEffect, useState } from 'react';
import axios from 'axios';

const genders = ['male', 'female', 'both'];

function TranscriptForm() {
  const [raiseReasons, setRaiseReasons] = useState<string[]>([]);
  const [lowerReasons, setLowerReasons] = useState<string[]>([]);
  const [allReasons, setAllReasons] = useState<string[]>([]);

  useEffect(() => {
    axios
      .get<string[]>('http://localhost:8080/reasons')
      .then((res) => setAllReasons(res.data))
      .catch((err) => console.error('Ошибка при получении reasons:', err));
  }, []);

  const handleSelect = (type: 'raise' | 'lower', value: string) => {
    const set = type === 'raise' ? setRaiseReasons : setLowerReasons;
    const current = type === 'raise' ? raiseReasons : lowerReasons;
    if (!current.includes(value) && value !== 'Выберите причину') {
      set([...current, value]);
    }
  };

  const handleRemove = (type: 'raise' | 'lower', value: string) => {
    const set = type === 'raise' ? setRaiseReasons : setLowerReasons;
    const current = type === 'raise' ? raiseReasons : lowerReasons;
    set(current.filter((r) => r !== value));
  };

  return (
    <form className="form-card">
      <h2>Транскрипция</h2>

      <label className="form-label">
        Англ название
        <input type="text" />
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

      <label className="form-label">
        Причины повышения
        <select onChange={(e) => handleSelect('raise', e.target.value)}>
          <option>Выберите причину</option>
          {allReasons.map((r) => (
            <option key={r}>{r}</option>
          ))}
        </select>
      </label>

      <div className="tag-container">
        {raiseReasons.map((r) => (
          <span key={r} className="tag">
            {r}
            <button type="button" onClick={() => handleRemove('raise', r)}>
              ×
            </button>
          </span>
        ))}
      </div>

      <label className="form-label">
        Причины понижения
        <select onChange={(e) => handleSelect('lower', e.target.value)}>
          <option>Выберите причину</option>
          {allReasons.map((r) => (
            <option key={r}>{r}</option>
          ))}
        </select>
      </label>

      <div className="tag-container">
        {lowerReasons.map((r) => (
          <span key={r} className="tag">
            {r}
            <button type="button" onClick={() => handleRemove('lower', r)}>
              ×
            </button>
          </span>
        ))}
      </div>

      <button type="submit">Отправить</button>
    </form>
  );
}

export default TranscriptForm;
