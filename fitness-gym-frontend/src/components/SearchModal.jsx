import { Search } from 'lucide-react';
import { useMemo, useState } from 'react';
import { Modal } from './Modal.jsx';

export function SearchModal({ exercises, classes, onClose, onExerciseSelect, onClassSelect }) {
  const [query, setQuery] = useState('');
  const normalizedQuery = query.trim().toLowerCase();

  const results = useMemo(() => {
    const exerciseResults = exercises
      .filter((item) => item.name.toLowerCase().includes(normalizedQuery) || item.focus.toLowerCase().includes(normalizedQuery))
      .map((item) => ({ type: 'exercise', label: item.name, meta: `${item.focus} - rekord ${item.record} kg`, item }));
    const classResults = classes
      .filter((item) => item.name.toLowerCase().includes(normalizedQuery) || item.level.toLowerCase().includes(normalizedQuery))
      .map((item) => ({ type: 'class', label: item.name, meta: `${item.time} - ${item.level}`, item }));

    return normalizedQuery ? [...exerciseResults, ...classResults] : [];
  }, [classes, exercises, normalizedQuery]);

  return (
    <Modal title="Wyszukiwarka" subtitle="Znajdź ćwiczenie albo zajęcia i przejdź od razu do akcji." onClose={onClose}>
      <label className="searchField">
        <Search size={18} />
        <input
          autoFocus
          value={query}
          onChange={(event) => setQuery(event.target.value)}
          placeholder="Np. martwy ciąg, box, siła"
        />
      </label>
      <div className="searchResults">
        {results.length === 0 && <p>Wpisz minimum kilka liter, żeby zobaczyć wyniki.</p>}
        {results.map((result) => (
          <button
            type="button"
            key={`${result.type}-${result.label}`}
            onClick={() => result.type === 'exercise' ? onExerciseSelect(result.item) : onClassSelect(result.item)}
          >
            <span>{result.type === 'exercise' ? 'Ćwiczenie' : 'Zajęcia'}</span>
            <b>{result.label}</b>
            <small>{result.meta}</small>
          </button>
        ))}
      </div>
    </Modal>
  );
}
