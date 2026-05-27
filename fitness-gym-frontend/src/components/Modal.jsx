import { X } from 'lucide-react';

export function Modal({ title, subtitle, children, onClose, size = 'normal' }) {
  return (
    <div className="modalBackdrop" onClick={onClose}>
      <section className={`modal ${size}`} onClick={(event) => event.stopPropagation()}>
        <button className="modalClose" type="button" onClick={onClose} aria-label="Zamknij">
          <X size={18} />
        </button>
        <h3>{title}</h3>
        {subtitle && <p className="modalSubtitle">{subtitle}</p>}
        {children}
      </section>
    </div>
  );
}
