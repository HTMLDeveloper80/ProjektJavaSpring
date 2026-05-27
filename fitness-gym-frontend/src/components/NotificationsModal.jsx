import { Bell, CalendarCheck, CreditCard, Dumbbell } from 'lucide-react';
import { Modal } from './Modal.jsx';

export function NotificationsModal({ reservations, startedWorkout, selectedPlan, onClose }) {
  const notifications = [
    {
      icon: CalendarCheck,
      title: reservations.length ? 'Masz aktywne rezerwacje' : 'Brak rezerwacji na dziś',
      text: reservations.length ? reservations.join(', ') : 'Zarezerwuj zajęcia z sekcji Start albo Zajęcia.'
    },
    {
      icon: Dumbbell,
      title: startedWorkout ? 'Trening jest w toku' : 'Dzisiejszy trening czeka',
      text: startedWorkout ? 'Sesja Push Day została oznaczona jako rozpoczęta.' : 'Kliknij Start w karcie aktywności.'
    },
    {
      icon: CreditCard,
      title: `Karnet ${selectedPlan}`,
      text: 'Wybrany plan jest widoczny też w sekcji rekordów.'
    }
  ];

  return (
    <Modal title="Powiadomienia" subtitle="Krótki podgląd aktywności w panelu." onClose={onClose}>
      <div className="notificationList">
        {notifications.map(({ icon: Icon, title, text }) => (
          <article key={title}>
            <Icon size={20} />
            <div>
              <b>{title}</b>
              <p>{text}</p>
            </div>
          </article>
        ))}
      </div>
      <button className="secondary fullWidth" type="button" onClick={onClose}>
        <Bell size={18} />
        Oznacz jako przeczytane
      </button>
    </Modal>
  );
}
