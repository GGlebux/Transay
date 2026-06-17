import { useEffect, useMemo, useRef, useState } from "react";
import "../../styles/combo.css";

export type SearchOption = { value: string; label: string; sub?: string };

/**
 * Комбобокс одиночного выбора с поиском.
 * В закрытом состоянии в поле показывается подпись выбранного значения,
 * при фокусе можно искать; поиск идёт и по label, и по sub (например, группе).
 */
export function SearchSelect({
  id,
  value,
  options,
  onChange,
  placeholder = "Поиск...",
}: {
  id?: string;
  value: string;
  options: SearchOption[];
  onChange: (value: string) => void;
  placeholder?: string;
}) {
  const [open, setOpen] = useState(false);
  const [search, setSearch] = useState("");
  const ref = useRef<HTMLDivElement>(null);

  const selectedLabel = useMemo(
    () => options.find((o) => o.value === value)?.label ?? "",
    [options, value]
  );

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return options;
    return options.filter(
      (o) =>
        o.label.toLowerCase().includes(q) ||
        (o.sub ?? "").toLowerCase().includes(q)
    );
  }, [options, search]);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (ref.current && !ref.current.contains(event.target as Node)) {
        setOpen(false);
        setSearch("");
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const pick = (v: string) => {
    onChange(v);
    setSearch("");
    setOpen(false);
  };

  return (
    <div className="combo" ref={ref}>
      <input
        id={id}
        type="text"
        className="combo__input"
        placeholder={placeholder}
        value={open ? search : selectedLabel}
        onChange={(e) => {
          setSearch(e.target.value);
          setOpen(true);
        }}
        onFocus={() => {
          setSearch("");
          setOpen(true);
        }}
      />
      {open && (
        <div className="combo__dropdown">
          {filtered.length > 0 ? (
            filtered.map((o) => (
              <div
                key={o.value}
                className={`combo__option ${o.value === value ? "combo__option--active" : ""}`}
                onClick={() => pick(o.value)}
              >
                <span>{o.label}</span>
                {o.sub && <span className="combo__option-sub">{o.sub}</span>}
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
