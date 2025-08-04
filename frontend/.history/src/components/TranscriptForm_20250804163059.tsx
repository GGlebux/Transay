import { useState } from 'react';

const genders = ['male', 'female', 'both'];
const exampleReasons = ['Стресс', 'Инфекция', 'Беременность', 'Физнагрузка'];

function TranscriptForm() {
  const [raiseReasons, setRaiseReasons] = useState<string[]>([]);
  const [lowerReasons, setLowerReasons] = useState<string[]>([]);

  const handleSelect = (type: 'raise' | 'lower', value: string) => {
    const set = type === 'raise' ? setRaiseReasons : setLowerReasons;
    const current = type === 'raise' ? raiseReasons : lowerReasons;
    if (!current.includes(value)) set([...current, value]);
  };

  const handleRemove = (type: 'raise' | 'lower', value: string) => {
    const set = type === 'raise' ? setRaiseReasons : setLowerReasons;
    const current = type === 'raise' ? raiseReasons : lowerReasons;
    set(current.filter((r) => r !== value));
  };

  return (
    <form className="form-card">
      <h2>Форма: Транскрипция</h2>

      <input placeholder="Англ название" />

      <select>
        {genders.map((g) => (
          <option key={g} value={g}>
            {g}
          </option>
        ))}
      </select>

      <label>Причины повышения</label>
      <select onChange={(e) => handleSelect('raise', e.target.value)}>
        <option>Выберите причину</option>
        {exampleReasons.map((r) => (
          <option key={r}>{r}</option>
        ))}
      </select>

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

      <label>Причины понижения</label>
      <select onChange={(e) => handleSelect('lower', e.target.value)}>
        <option>Выберите причину</option>
        {exampleReasons.map((r) => (
          <option key={r}>{r}</option>
        ))}
      </select>

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
