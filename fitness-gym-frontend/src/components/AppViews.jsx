import { useState } from 'react';
import {
  ArrowRight,
  BadgeCheck,
  BarChart3,
  CalendarCheck,
  CalendarDays,
  CheckCircle2,
  Clock,
  CreditCard,
  Dumbbell,
  QrCode,
  Star,
  Trophy,
  UserPlus
} from 'lucide-react';

const weekDays = [
  { date: '2026-05-25', label: 'Pon', day: '25' },
  { date: '2026-05-26', label: 'Wt', day: '26' },
  { date: '2026-05-27', label: 'Śr', day: '27' },
  { date: '2026-05-28', label: 'Czw', day: '28' },
  { date: '2026-05-29', label: 'Pt', day: '29' },
  { date: '2026-05-30', label: 'Sob', day: '30' },
  { date: '2026-05-31', label: 'Nd', day: '31' }
];

export function DashboardView({ user, isLoggedIn, dashboard, nextClass, reservations, selectedPlan, ranking, onReserve, onStartWorkout, onCheckIn, onNavigate, onRegister }) {
  return (
    <section className="viewGrid dashboardGrid">
      <div className="heroPanel">
        <span className="eyebrow"><BadgeCheck size={16} /> Panel klubowicza</span>
        <h1>{isLoggedIn ? `Cześć, ${user.name}.` : 'Stwórz swój panel treningowy.'} Trening, wejścia i rezerwacje w jednym miejscu.</h1>
        <p>Sprawdzaj zajęcia, startuj sesje treningowe, wybieraj karnety i pilnuj rekordów bez skakania po przypadkowych ekranach.</p>
        <div className="heroActions">
          <button className="primary" type="button" onClick={() => isLoggedIn ? onStartWorkout('Trening dnia') : onRegister()}>
            {isLoggedIn ? <Dumbbell size={18} /> : <UserPlus size={18} />} {isLoggedIn ? 'Rozpocznij trening' : 'Załóż konto'}
          </button>
          <button className="secondary" type="button" onClick={onCheckIn}>
            <QrCode size={18} /> Wejście QR
          </button>
        </div>
      </div>

      <div className="metricPanel">
        <span>Postęp tygodnia</span>
        <strong>{dashboard?.weeklyProgressPercent ?? (isLoggedIn ? 0 : 42)}%</strong>
        <div className="meter"><i style={{ width: `${dashboard?.weeklyProgressPercent ?? (isLoggedIn ? 0 : 42)}%` }} /></div>
        <small>{dashboard?.completedSessions ?? 0} zakończone sesje w tym tygodniu</small>
      </div>

      <div className="quickGrid">
        <QuickCard icon={CalendarCheck} label="Rezerwacje" value={reservations.length} onClick={() => onNavigate('classes')} />
        <QuickCard icon={CreditCard} label="Karnet" value={selectedPlan} onClick={() => onNavigate('plans')} />
        <QuickCard icon={Trophy} label="Rekordy" value={dashboard?.personalRecords ?? 0} onClick={() => onNavigate('records')} />
        <QuickCard icon={Star} label="Punkty" value={isLoggedIn ? (dashboard?.points ?? ranking[0]?.points ?? 0) : 0} onClick={() => onNavigate('records')} />
      </div>

      <article className="panel nextClass">
        <div className="sectionHead">
          <div>
            <span>Następne zajęcia</span>
            <h2>{nextClass?.name ?? 'Brak zajęć'}</h2>
          </div>
          <CalendarDays />
        </div>
        <p>{nextClass ? `${nextClass.date} o ${nextClass.time} · poziom ${nextClass.level}` : 'Grafik jest pusty.'}</p>
        {nextClass && (
          <button className="primary" type="button" onClick={() => onReserve(nextClass)}>
            Zarezerwuj miejsce <ArrowRight size={18} />
          </button>
        )}
      </article>
    </section>
  );
}

export function ClassesView({ classes, activeDate, reservations, onDateChange, onReserve }) {
  const dayClasses = classes.filter((item) => item.date === activeDate);

  return (
    <section className="viewStack">
      <ViewTitle eyebrow="Grafik i rezerwacje" title="Kalendarz zajęć" text="Wybierz konkretny dzień i zapisz się tylko na wybrany termin." />
      <div className="calendarStrip">
        {weekDays.map((day) => (
          <button key={day.date} type="button" className={activeDate === day.date ? 'active' : ''} onClick={() => onDateChange(day.date)}>
            <span>{day.label}</span>
            <b>{day.day}</b>
          </button>
        ))}
      </div>
      <div className="classBoard">
        <div className="scheduleList">
          {dayClasses.length === 0 && (
            <article className="emptyPanel">
              <h3>Brak zajęć tego dnia</h3>
              <p>Wybierz inny dzień tygodnia w kalendarzu.</p>
            </article>
          )}
          {dayClasses.map((item) => {
            const reserved = reservations.some((reservation) => reservation.id === item.id);
            return (
              <article className={`scheduleItem ${reserved ? 'reserved' : ''}`} key={`${item.id}-${item.date}-${item.time}`}>
                <time>{item.time}</time>
                <div>
                  <h3>{item.name}</h3>
                  <p>{item.date} · poziom {item.level} · limit {item.capacity} osób</p>
                </div>
                <button className={reserved ? 'secondary' : 'primary'} type="button" onClick={() => onReserve(item)}>
                  {reserved ? 'Zapisano' : 'Zapisz się'}
                </button>
              </article>
            );
          })}
        </div>
        <aside className="panel reservationSummary">
          <h3>Twoje zapisy</h3>
          {reservations.length === 0 && <p>Nie masz jeszcze aktywnych rezerwacji.</p>}
          {reservations.map((item) => (
            <div className="miniRow" key={`${item.id}-${item.date}`}>
              <CalendarCheck size={18} />
              <span>{item.name}<small>{item.date}, {item.time}</small></span>
            </div>
          ))}
        </aside>
      </div>
    </section>
  );
}

export function TrainersView({ trainers, onCreatePlan }) {
  const fallback = [
    { id: 'T-ADAM', name: 'Adam Nowak', specialization: 'siła, redukcja, technika bojów' },
    { id: 'T-KASIA', name: 'Kasia Zielińska', specialization: 'mobilność, stretching, trening funkcjonalny' },
    { id: 'T-MICHAL', name: 'Michał Wójcik', specialization: 'trening motoryczny, przygotowanie sportowe, interwały' },
    { id: 'T-OLA', name: 'Ola Mazur', specialization: 'trening kobiet, sylwetka, zdrowy kręgosłup' }
  ];
  const list = trainers.length ? trainers : fallback;

  return (
    <section className="viewStack">
      <ViewTitle eyebrow="Zespół trenerów" title="Trenerzy i plany" text="Wybierz trenera i wygeneruj plan treningowy przez moduł ASP.NET Core." />
      <div className="trainerGrid">
        {list.map((trainer, index) => (
          <article className="trainerCard" key={trainer.id}>
            <div className="avatar">{trainer.name.split(' ').map((part) => part[0]).join('')}</div>
            <h3>{trainer.name}</h3>
            <p>{trainer.specialization}</p>
            <div className="ratingLine"><Star fill="currentColor" size={18} /> {(4.9 - index * 0.1).toFixed(1)} · wolne terminy</div>
            <button className="primary" type="button" onClick={() => onCreatePlan(index % 2 === 0 ? 'siła i redukcja' : 'mobilność i zdrowe plecy', index % 2 === 0 ? 'średni' : 'lekki')}>
              Utwórz plan
            </button>
          </article>
        ))}
      </div>
    </section>
  );
}

export function PlansView({ plans, selectedPlan, onChoosePlan }) {
  return (
    <section className="viewStack">
      <ViewTitle eyebrow="Karnety" title="Wybierz plan członkowski" text="Wybór karnetu otworzy płatność demo bez podawania karty." />
      <div className="planGrid">
        {plans.map((plan) => (
          <article className={`plan ${plan.name === selectedPlan ? 'selectedPlan' : ''}`} key={plan.name}>
            <span className="tag">{plan.name === selectedPlan ? 'Aktywny' : plan.tag}</span>
            <h3>{plan.name}</h3>
            <div className="price">{plan.price}<small>/ mies.</small></div>
            {plan.perks.map((perk) => <p key={perk}><CheckCircle2 size={17} /> {perk}</p>)}
            <button type="button" onClick={() => onChoosePlan(plan.name)}>{plan.name === selectedPlan ? 'Przedłuż demo' : 'Wybierz i opłać demo'}</button>
          </article>
        ))}
      </div>
    </section>
  );
}

export function JournalView({ exercises, session, selectedExercise, entries, onSelectExercise, onStartWorkout, onCreatePlan, onAddEntry }) {
  const exerciseName = selectedExercise?.name ?? selectedExercise?.Name ?? 'Ćwiczenie';
  const exerciseId = selectedExercise?.id ?? selectedExercise?.Id ?? exerciseName;
  const muscles = selectedExercise?.muscles ?? selectedExercise?.Muscles ?? [];
  const [draft, setDraft] = useState({ sets: 4, reps: 8, weight: 60, note: '' });

  function submitEntry(event) {
    event.preventDefault();
    onAddEntry({
      exerciseId,
      exerciseName,
      sets: Number(draft.sets),
      reps: Number(draft.reps),
      weight: Number(draft.weight),
      note: draft.note
    });
    setDraft((current) => ({ ...current, note: '' }));
  }

  return (
    <section className="viewStack">
      <ViewTitle eyebrow="Dziennik treningowy" title="Sesje, ćwiczenia i plan dnia" text="Kliknięcie ćwiczenia zmienia szczegóły, plan serii i formularz wpisu." />
      <div className="journalGrid">
        <div className="panel journalDetail">
          <div className="sectionHead">
            <div>
              <span>Wybrane ćwiczenie</span>
              <h2>{exerciseName}</h2>
            </div>
            <Dumbbell />
          </div>
          <p>{selectedExercise?.description ?? selectedExercise?.Description ?? `Kategoria: ${selectedExercise?.category ?? selectedExercise?.focus ?? 'siła'}.`}</p>
          <div className="chipRow">
            <span>{selectedExercise?.difficulty ?? selectedExercise?.Difficulty ?? 'średni'}</span>
            {(muscles.length ? muscles : [selectedExercise?.focus ?? 'całe ciało']).map((item) => <span key={item}>{item}</span>)}
          </div>
          <form className="trainingForm" onSubmit={submitEntry}>
            <label>Serie<input type="number" min="1" value={draft.sets} onChange={(event) => setDraft({ ...draft, sets: event.target.value })} /></label>
            <label>Powtórzenia<input type="number" min="1" value={draft.reps} onChange={(event) => setDraft({ ...draft, reps: event.target.value })} /></label>
            <label>Ciężar kg<input type="number" min="0" step="0.5" value={draft.weight} onChange={(event) => setDraft({ ...draft, weight: event.target.value })} /></label>
            <label className="wideInput">Notatka<input value={draft.note} onChange={(event) => setDraft({ ...draft, note: event.target.value })} placeholder="np. ostatnia seria ciężka" /></label>
            <button className="primary" type="submit">Dodaj wpis</button>
          </form>
          <div className="formRow">
            <button className="secondary" type="button" onClick={() => onStartWorkout(exerciseName)}>Start sesji</button>
            <button className="secondary" type="button" onClick={() => onCreatePlan(exerciseName, selectedExercise?.difficulty ?? 'średni')}>Plan pod to ćwiczenie</button>
          </div>
        </div>
        <div className="exerciseGrid">
          {exercises.map((exercise) => {
            const name = exercise.name ?? exercise.Name;
            const category = exercise.category ?? exercise.focus ?? 'Siła';
            const selected = (selectedExercise?.name ?? selectedExercise?.Name) === name;
            return (
              <button className={`exerciseTile ${selected ? 'active' : ''}`} key={name} type="button" onClick={() => onSelectExercise(exercise)}>
                <b>{name}</b>
                <span>{category}</span>
              </button>
            );
          })}
        </div>
      </div>
      <div className="panel trainingLog">
        <h3>Wpisy z tej sesji</h3>
        {entries.length === 0 && <p>Dodaj pierwsze ćwiczenie z formularza w dzienniku.</p>}
        {entries.map((entry) => (
          <div className="miniRow" key={entry.id}>
            <Clock size={18} />
            <span>{entry.exerciseName}<small>{entry.sets} x {entry.reps} · {entry.weight} kg · {entry.createdAt}{entry.note ? ` · ${entry.note}` : ''}</small></span>
          </div>
        ))}
        <p className="sessionNote">{session?.summary ?? 'Sesja nie jest jeszcze aktywna w module .NET.'}</p>
      </div>
    </section>
  );
}

export function RecordsView({ records, ranking, selectedExercise, reservations, exercises, onAddRecord }) {
  const currentName = selectedExercise?.name ?? selectedExercise?.Name ?? exercises[0]?.name ?? exercises[0]?.Name ?? 'Własny rekord';
  const currentId = selectedExercise?.id ?? selectedExercise?.Id ?? currentName;
  const [draft, setDraft] = useState({ exerciseName: currentName, exerciseId: currentId, value: '', unit: 'kg' });

  function submitRecord(event) {
    event.preventDefault();
    onAddRecord(draft);
    setDraft((current) => ({ ...current, value: '' }));
  }

  return (
    <section className="viewStack">
      <ViewTitle eyebrow="Postępy" title="Rekordy i ranking" text="Rekordy można teraz dodać ręcznie, a po uruchomieniu .NET zapis przechodzi przez Spring API." />
      <div className="recordsGrid">
        <div className="panel">
          <h3>Rekordy osobiste</h3>
          {(records.length ? records : fallbackRecords(selectedExercise)).map((record) => (
            <div className="miniRow" key={`${record.exerciseId ?? record.exerciseName}-${record.value}`}>
              <Trophy size={18} />
              <span>{record.exerciseName ?? record.name}<small>{record.value ?? record.record} {record.unit ?? 'kg'}</small></span>
            </div>
          ))}
        </div>
        <form className="panel recordForm" onSubmit={submitRecord}>
          <h3>Dodaj rekord</h3>
          <label>Ćwiczenie<input value={draft.exerciseName} onChange={(event) => setDraft({ ...draft, exerciseName: event.target.value })} required /></label>
          <label>Wynik<input type="number" min="0.1" step="0.1" value={draft.value} onChange={(event) => setDraft({ ...draft, value: event.target.value })} required /></label>
          <label>Jednostka<input value={draft.unit} onChange={(event) => setDraft({ ...draft, unit: event.target.value })} required /></label>
          <button className="primary" type="submit">Zapisz rekord</button>
        </form>
        <div className="panel">
          <h3>Ranking klubowy</h3>
          {(ranking.length ? ranking : [{ displayName: 'Klubowicz Demo', points: 1260, position: 1 }]).map((entry) => (
            <div className="rankingRow" key={entry.memberId ?? entry.displayName}>
              <b>#{entry.position}</b>
              <span>{entry.displayName}</span>
              <strong>{entry.points} pkt</strong>
            </div>
          ))}
          <MetricLine icon={CalendarCheck} label="Rezerwacje" value={reservations.length} />
          <MetricLine icon={BarChart3} label="Trend" value="+12%" />
        </div>
      </div>
    </section>
  );
}

export function ProfileView({ user, isLoggedIn, selectedPlan, reservations, onCheckIn, onLogout, onRegister }) {
  if (!isLoggedIn) {
    return (
      <section className="viewStack">
        <ViewTitle eyebrow="Konto" title="Profil klubowicza" text="Załóż konto, żeby widzieć karnet, QR i własne rezerwacje." />
        <div className="panel profileGuest">
          <UserPlus size={42} />
          <h2>Nie jesteś zalogowany</h2>
          <p>Profil nie pokazuje już gotowego konta testowego. Dane pojawią się po rejestracji albo logowaniu.</p>
          <button className="primary" type="button" onClick={onRegister}>Załóż konto</button>
        </div>
      </section>
    );
  }

  return (
    <section className="viewStack">
      <ViewTitle eyebrow="Konto" title="Profil klubowicza" text="Tu widzisz dane, aktywny karnet, kod QR i najbliższe rezerwacje." />
      <div className="profileGrid">
        <div className="panel profileCard">
          <div className="avatar large">{user.name.slice(0, 2).toUpperCase()}</div>
          <h2>{user.name}</h2>
          <p>{user.email}</p>
          <span className="tag">Karnet {selectedPlan}</span>
        </div>
        <div className="panel qrPanel">
          <QrCode size={58} />
          <h3>{user.qrCode}</h3>
          <p>Kod do kontroli wejścia na siłowni.</p>
          <button className="primary" type="button" onClick={onCheckIn}>Zasymuluj wejście</button>
        </div>
        <div className="panel">
          <h3>Rezerwacje</h3>
          {reservations.length === 0 && <p>Brak aktywnych rezerwacji.</p>}
          {reservations.map((item) => (
            <div className="miniRow" key={`${item.id}-${item.date}`}>
              <Clock size={18} />
              <span>{item.name}<small>{item.date}, {item.time}</small></span>
            </div>
          ))}
          <button className="secondary fullWidth" type="button" onClick={onLogout}>Wyloguj</button>
        </div>
      </div>
    </section>
  );
}

function ViewTitle({ eyebrow, title, text }) {
  return (
    <header className="viewTitle">
      <span>{eyebrow}</span>
      <h1>{title}</h1>
      <p>{text}</p>
    </header>
  );
}

function QuickCard({ icon: Icon, label, value, onClick }) {
  return (
    <button className="quickCard" type="button" onClick={onClick}>
      <Icon size={22} />
      <span>{label}</span>
      <b>{value}</b>
    </button>
  );
}

function MetricLine({ icon: Icon, label, value }) {
  return (
    <div className="metricLine">
      <Icon size={18} />
      <span>{label}</span>
      <b>{value}</b>
    </div>
  );
}

function fallbackRecords(selectedExercise) {
  return [
    { name: selectedExercise?.name ?? selectedExercise?.Name ?? 'Przysiad ze sztangą', record: selectedExercise?.record ?? 140 },
    { name: 'Wyciskanie na ławce', record: 105 },
    { name: 'Martwy ciąg', record: 180 }
  ];
}
