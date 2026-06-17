import { useEffect, useMemo, useRef, useState } from "react";
import "../../styles/combo.css";

type Reason = { id: number; name: string };

/**
 * Комбобокс множественного выбора с поиском.
 * Выбранные значения показываются чипами над полем ввода; клик по чипу убирает его.
 * В выпадающем списке остаются только ещё не выбранные варианты — клик добавляет.
 */
export function MultiSelectWithSearch({
  options,
  selected,
  onChange,
  placeholder = "Поиск...",
}: {
  label?: string;
  options: Reason[];
  selected: number[];
  onChange: (ids: number[]) => void;
  placeholder?: string;
}) {
  const [open, setOpen] = useState(false);
  const [search, setSearch] = useState("");
  const ref = useRef<HTMLDivElement>(null);

  const selectedOptions = useMemo(
    () =>
      selected
        .map((id) => options.find((o) => o.id === id))
        .filter((o): o is Reason => Boolean(o)),
    [selected, options]
  );

  const filtered = useMemo(
    () =>
      options.filter(
        (o) =>
          !selected.includes(o.id) &&
          o.name.toLowerCase().includes(search.toLowerCase())
      ),
    [options, selected, search]
  );

  const add = (id: number) => {
    onChange([...selected, id]);
    setSearch("");
  };
  const remove = (id: number) => onChange(selected.filter((s) => s !== id));

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (ref.current && !ref.current.contains(event.target as Node)) {
        setOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div className="combo" ref={ref}>
      {selectedOptions.length > 0 && (
        <div className="combo__tags">
          {selectedOptions.map((o) => (
            <span
              key={o.id}
              className="combo__tag"
              onClick={() => remove(o.id)}
              title="Убрать"
            >
              {o.name}
              <span className="combo__tag-x" aria-hidden="true">
                ×
              </span>
            </span>
          ))}
        </div>
      )}

      <input
        type="text"
        className="combo__input"
        placeholder={placeholder}
        value={search}
        onChange={(e) => {
          setSearch(e.target.value);
          setOpen(true);
        }}
        onFocus={() => setOpen(true)}
      />

      {open && (
        <div className="combo__dropdown">
          {filtered.length > 0 ? (
            filtered.map((o) => (
              <div
                key={o.id}
                className="combo__option"
                onClick={() => add(o.id)}
              >
                {o.name}
              </div>
            ))
          ) : (
            <div className="combo__empty">Ничего не найдено</div>
          )}
        </div>
      )}
    </div>
  );
}
