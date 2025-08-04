import { useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';

type Reason = { id: number; name: string };

const genders = ['male', 'female', 'both'];

function TranscriptForm() {
  const [allReasons, setAllReasons] = useState<Reason[]>([]);
  const [raiseReasons, setRaiseReasons] = useState<Reason[]>([]);
  const [lowerReasons, setLowerReasons] = useState<Reason[]>([]);
  const [name, setName] = useState('');
  const [gender, setGender] = useState('male');

  useEffect(() => {
    axios
      .get<Reason[]>('http://localhost:8080/reasons')
      .then((res) => {
        const sorted = res.data.sort((a, b) => a.name.localeCompare(b.name));
        setAllReasons(sorted);
      })
      .catch((err) => console.error('Ошибка при получении reasons:', err));
  }, []);

  const handleSelect = (type: 'raise' | 'lower', id: number) => {
    const reason = allReasons.find((r) => r.id === id);
    if (!reason) return;

    const list = type === 'raise' ? raiseReasons : lowerReasons;
    const set = type === 'raise' ? setRaiseReasons : setLowerReasons;

    if (!list.some((r) => r.id === id)) {
      set([...list, reason]);
    }
  };

  const handleRemove = (type: 'raise' | 'lower', id: number) => {
    const set = type === 'raise' ? setRaiseReasons : setLowerReasons;
    const list = type === 'raise' ? raiseReasons : lowerReasons;

    set(list.filter((r) => r.id !== id));
  };

const onSubmit = (e: FormEvent) => {
  e.preventDefault();

  const fallsIds = lowerReasons.map(r => r.id);
  const raisesIds = raiseReasons.map(r => r.id);

  const payload = {
    name,
    gender,
    fallsIds,
    raisesIds,
  };

  axios
    .post('http://localhost:8080/transcripts', payload)
    .then(() => alert('Transcript sent'))
    .catch((err) => console.error('Ошибка отправки transcript:', err));
};



  return (
    <form className="form-card" onSubmit={onSubmit}>
      <h2>Транскрипция</h2>

      <label className="form-label">
        Англ название
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
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

      <label className="form-label">
        Причины повышения
        <select onChange={(e) => handleSelect('raise', Number(e.target.value))} defaultValue="">
          <option value="">Выберите причину</option>
          {allReasons.map((r) => (
            <option key={r.id} value={r.id}>
              {r.name}
            </option>
          ))}
        </select>
      </label>

      <div className="tag-container">
        {raiseReasons.map((r) => (
          <span key={r.id} className="tag">
            {r.name}
            <button type="button" onClick={() => handleRemove('raise', r.id)}>×</button>
          </span>
        ))}
      </div>

      <label className="form-label">
        Причины понижения
        <select onChange={(e) => handleSelect('lower', Number(e.target.value))} defaultValue="">
          <option value="">Выберите причину</option>
          {allReasons.map((r) => (
            <option key={r.id} value={r.id}>
              {r.name}
            </option>
          ))}
        </select>
      </label>

      <div className="tag-container">
        {lowerReasons.map((r) => (
          <span key={r.id} className="tag">
            {r.name}
            <button type="button" onClick={() => handleRemove('lower', r.id)}>×</button>
          </span>
        ))}
      </div>

      <button type="submit">Отправить</button>
    </form>
  );
}

export default TranscriptForm;
