import { PlayCircle } from 'lucide-react';
import { Modal } from './Modal.jsx';

export function VideoModal({ onClose }) {
  return (
    <Modal
      title="Demo panelu"
      subtitle="Krótki podgląd przepływu: rezerwacja, trening, rekordy i karnet."
      onClose={onClose}
      size="wide"
    >
      <div className="demoPreview">
        <div className="demoScreen">
          <PlayCircle size={54} />
          <b>FitCore flow</b>
          <span>Rezerwuj zajęcia, startuj trening i śledź progres z jednego panelu.</span>
        </div>
        <ol>
          <li>Wybierz najbliższe zajęcia z grafiku.</li>
          <li>Uruchom sesję treningową z karty tygodnia.</li>
          <li>Sprawdź rekordy w dzienniku ćwiczeń.</li>
        </ol>
      </div>
    </Modal>
  );
}
