import { useEffect, useMemo, useState } from 'react';
import { Activity, Bell, CalendarDays, CreditCard, Dumbbell, Search, UserRound } from 'lucide-react';
import { Topbar } from './components/Topbar.jsx';
import { AuthModal } from './components/AuthModal.jsx';
import { Modal } from './components/Modal.jsx';
import {
  ClassesView,
  DashboardView,
  JournalView,
  PlansView,
  ProfileView,
  RecordsView,
  TrainersView
} from './components/AppViews.jsx';
import { classes as fallbackClasses, exercises as fallbackExercises, plans as fallbackPlans } from './data/content.js';
import {
  getFromApi,
  getStoredAuthSession,
  logoutFromApi,
  postToApi,
  saveAuthSession
} from './services/api.js';

const navItems = [
  { id: 'dashboard', label: 'Panel', icon: Activity },
  { id: 'classes', label: 'Zajęcia', icon: CalendarDays },
  { id: 'trainers', label: 'Trenerzy', icon: UserRound },
  { id: 'plans', label: 'Karnety', icon: Dumbbell },
  { id: 'journal', label: 'Dziennik', icon: Activity },
  { id: 'records', label: 'Rekordy', icon: Bell },
  { id: 'profile', label: 'Profil', icon: UserRound }
];

const guestUser = {
  memberId: null,
  name: 'Gość',
  email: '',
  qrCode: ''
};

export default function App() {
  const [activeView, setActiveView] = useState('dashboard');
  const [apiState, setApiState] = useState({ status: 'idle', message: 'System gotowy do pracy.' });
  const [authOpen, setAuthOpen] = useState(false);
  const [authMode, setAuthMode] = useState('register');
  const [authUser, setAuthUser] = useState(getStoredAuthSession);
  const [searchOpen, setSearchOpen] = useState(false);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [paymentOpen, setPaymentOpen] = useState(false);
  const [paymentDraft, setPaymentDraft] = useState(null);
  const [classes, setClasses] = useState(fallbackClasses);
  const [trainers, setTrainers] = useState([]);
  const [exercises, setExercises] = useState(fallbackExercises);
  const [dashboard, setDashboard] = useState(null);
  const [records, setRecords] = useState([]);
  const [ranking, setRanking] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [selectedPlan, setSelectedPlan] = useState('Brak');
  const [selectedExercise, setSelectedExercise] = useState(fallbackExercises[0]);
  const [activeDate, setActiveDate] = useState('2026-05-25');
  const [session, setSession] = useState(null);
  const [trainingEntries, setTrainingEntries] = useState([]);

  const user = authUser ?? guestUser;

  useEffect(() => {
    loadInitialData();
  }, [authUser?.memberId]);

  useEffect(() => {
    saveAuthSession(authUser);
  }, [authUser]);

  useEffect(() => {
    function expireSession() {
      setAuthUser(null);
      setApiState({ status: 'warning', message: 'Sesja wygasla. Zaloguj sie ponownie.' });
    }

    function updateSession(event) {
      setAuthUser((current) => current ? { ...current, ...event.detail } : current);
    }

    window.addEventListener('fitcore-auth-expired', expireSession);
    window.addEventListener('fitcore-auth-refreshed', updateSession);
    return () => {
      window.removeEventListener('fitcore-auth-expired', expireSession);
      window.removeEventListener('fitcore-auth-refreshed', updateSession);
    };
  }, []);

  async function loadInitialData() {
    const [classesResult, trainersResult, exercisesResult, rankingResult] = await Promise.all([
      getFromApi('/classes'),
      getFromApi('/trainers'),
      getFromApi('/fitness/exercises'),
      getFromApi('/fitness/gamification/ranking')
    ]);

    if (classesResult.ok && Array.isArray(classesResult.data)) {
      setClasses(classesResult.data);
    }
    if (trainersResult.ok && Array.isArray(trainersResult.data)) {
      setTrainers(trainersResult.data);
    }
    if (exercisesResult.ok && Array.isArray(exercisesResult.data)) {
      setExercises(exercisesResult.data);
      setSelectedExercise(exercisesResult.data[0] ?? fallbackExercises[0]);
    }
    if (rankingResult.ok && Array.isArray(rankingResult.data)) {
      setRanking(rankingResult.data);
    }

    if (authUser?.memberId) {
      const [dashboardResult, recordsResult, reservationsResult] = await Promise.all([
        getFromApi(`/fitness/members/${authUser.memberId}/dashboard`),
        getFromApi(`/fitness/members/${authUser.memberId}/records`),
        getFromApi(`/classes/reservations/${authUser.memberId}`)
      ]);

      if (dashboardResult.ok) {
        setDashboard(dashboardResult.data);
      }
      if (recordsResult.ok && Array.isArray(recordsResult.data)) {
        setRecords(recordsResult.data);
      }
      if (reservationsResult.ok && Array.isArray(reservationsResult.data)) {
        setReservations(normalizeClasses(reservationsResult.data));
      }
    } else {
      setDashboard(null);
      setRecords([]);
      setReservations([]);
    }

    if ([classesResult, trainersResult].every((result) => result.ok)) {
      setApiState({ status: 'success', message: 'Dane zostały pobrane ze Spring API.' });
    } else {
      setApiState({ status: 'warning', message: 'Część danych działa lokalnie. Uruchom Spring i .NET, żeby mieć pełny tryb.' });
    }
  }

  function requireAccount(actionText = 'Ta akcja wymaga konta.') {
    if (authUser?.memberId) {
      return true;
    }

    setAuthMode('register');
    setAuthOpen(true);
    setApiState({ status: 'warning', message: `${actionText} Załóż konto albo zaloguj się, żeby zapisać dane.` });
    return false;
  }

  async function submitAuth(form) {
    const payload = authMode === 'register'
      ? { name: form.name.trim(), email: form.email.trim(), password: form.password }
      : { email: form.email.trim(), password: form.password };

    const result = await postToApi(authMode === 'register' ? '/auth/register' : '/auth/login', payload);
    if (!result.ok) {
      setApiState({ status: 'error', message: result.data?.message ?? 'Nie udało się zalogować. Sprawdź e-mail albo załóż nowe konto.' });
      return;
    }

    const data = result.data && typeof result.data === 'object' ? result.data : {};
    setAuthUser({
      memberId: data.memberId,
      name: data.name ?? payload.name ?? payload.email.split('@')[0],
      email: data.email ?? payload.email,
      qrCode: data.qrCode ?? `QR-${data.memberId}`,
      accessToken: data.accessToken,
      refreshToken: data.refreshToken,
      tokenType: data.tokenType,
      expiresIn: data.expiresIn
    });
    setAuthOpen(false);
    setApiState({ status: 'success', message: authMode === 'register' ? 'Konto utworzone i aktywne.' : 'Zalogowano do panelu.' });
    setActiveView('dashboard');
  }

  function openAuth(mode) {
    setAuthMode(mode);
    setAuthOpen(true);
  }

  async function logout() {
    await logoutFromApi();
    setAuthUser(null);
    setSelectedPlan('Brak');
    setActiveView('dashboard');
    setApiState({ status: 'idle', message: 'Wylogowano użytkownika.' });
  }

  async function reserveClass(classItem) {
    if (!requireAccount('Zapis na zajęcia wymaga konta.')) {
      return;
    }

    const alreadyReserved = reservations.some((item) => item.id === classItem.id);
    if (alreadyReserved) {
      setReservations((current) => current.filter((item) => item.id !== classItem.id));
      setApiState({ status: 'idle', message: `Usunięto lokalnie rezerwację: ${classItem.name}.` });
      return;
    }

    const result = await postToApi('/classes/reservations', {
      memberId: authUser.memberId,
      classId: classItem.id,
      className: classItem.name,
      user: authUser.email
    });

    if (result.ok) {
      const freshReservations = await getFromApi(`/classes/reservations/${authUser.memberId}`);
      setReservations(freshReservations.ok && Array.isArray(freshReservations.data)
        ? normalizeClasses(freshReservations.data)
        : (current) => [...current, classItem]);
      setApiState({ status: 'success', message: `Zapisano na ${classItem.name} w dniu ${classItem.date}.` });
    } else {
      setApiState({ status: 'error', message: result.data?.message ?? 'Nie udało się zapisać na zajęcia.' });
    }
  }

  function choosePlan(planName) {
    if (!requireAccount('Wybór karnetu wymaga konta.')) {
      return;
    }

    const plan = fallbackPlans.find((item) => item.name === planName);
    setPaymentDraft({ planName, price: plan?.price ?? '', method: 'BLIK demo' });
    setPaymentOpen(true);
  }

  async function confirmPayment(method) {
    const planName = paymentDraft?.planName;
    if (!planName || !authUser?.memberId) {
      return;
    }

    const result = await postToApi('/plans/select', { memberId: authUser.memberId, planName, user: authUser.email });
    if (result.ok) {
      setSelectedPlan(planName);
      setPaymentOpen(false);
      setApiState({ status: 'success', message: `Płatność demo (${method}) zaakceptowana. Karnet ${planName} jest aktywny.` });
    } else {
      setApiState({ status: 'error', message: 'Nie udało się zmienić karnetu.' });
    }
  }

  async function checkIn() {
    if (!requireAccount('Wejście QR wymaga konta.')) {
      return;
    }

    const result = await postToApi('/access/checkin', { memberId: authUser.memberId, qrCode: authUser.qrCode });
    setApiState({
      status: result.ok ? 'success' : 'error',
      message: result.data?.message ?? (result.ok ? 'Wejście potwierdzone.' : 'Wejście odrzucone.')
    });
  }

  async function startWorkout(workoutName = 'Trening dnia') {
    if (!requireAccount('Dziennik treningowy wymaga konta.')) {
      return;
    }

    const result = await postToApi('/workouts/start', { memberId: authUser.memberId, workoutName, user: authUser.email });
    if (result.ok) {
      setSession(result.data);
      setApiState({ status: 'success', message: result.data?.summary ?? 'Sesja treningowa rozpoczęta.' });
      setActiveView('journal');
    } else {
      setApiState({ status: 'error', message: 'Nie udało się rozpocząć treningu.' });
    }
  }

  async function createTrainingPlan(goal, level) {
    if (!requireAccount('Generowanie planu wymaga konta.')) {
      return;
    }

    const result = await postToApi(`/members/${authUser.memberId}/training-plan`, { goal, level });
    setApiState({
      status: result.ok ? 'success' : 'error',
      message: result.ok ? `Wygenerowano plan: ${result.data?.title ?? goal}.` : 'Nie udało się wygenerować planu.'
    });
    if (result.ok) {
      setActiveView('journal');
    }
  }

  function addTrainingEntry(entry) {
    if (!requireAccount('Dodanie wpisu do dziennika wymaga konta.')) {
      return;
    }

    setTrainingEntries((current) => [{ ...entry, id: crypto.randomUUID(), createdAt: new Date().toLocaleString('pl-PL') }, ...current]);
    setApiState({ status: 'success', message: `Dodano wpis do dziennika: ${entry.exerciseName}.` });
  }

  async function addRecord(record) {
    if (!requireAccount('Dodanie rekordu wymaga konta.')) {
      return;
    }

    const result = await postToApi(`/fitness/members/${authUser.memberId}/records`, {
      memberId: authUser.memberId,
      exerciseId: record.exerciseId,
      exerciseName: record.exerciseName,
      value: Number(record.value),
      unit: record.unit
    });

    if (result.ok) {
      const freshRecords = await getFromApi(`/fitness/members/${authUser.memberId}/records`);
      setRecords(freshRecords.ok && Array.isArray(freshRecords.data) ? freshRecords.data : (current) => [result.data, ...current]);
      setApiState({ status: 'success', message: `Dodano rekord: ${record.exerciseName}.` });
    } else {
      setApiState({ status: 'error', message: result.data?.message ?? 'Nie udało się dodać rekordu.' });
    }
  }

  const normalizedClasses = useMemo(() => normalizeClasses(classes), [classes]);
  const nextClass = normalizedClasses.find((item) => item.date >= activeDate) ?? normalizedClasses[0];

  return (
    <div className="appShell">
      <Topbar
        navItems={navItems}
        activeView={activeView}
        onViewChange={setActiveView}
        authUser={authUser}
        onLogin={() => openAuth('login')}
        onRegister={() => openAuth('register')}
        onLogout={logout}
        onSearch={() => setSearchOpen(true)}
        onNotifications={() => setNotificationsOpen(true)}
        onClientPanel={() => setActiveView('profile')}
      />

      <div className={`systemBanner ${apiState.status}`}>{apiState.message}</div>

      <main className="workspace">
        {activeView === 'dashboard' && (
          <DashboardView
            user={user}
            isLoggedIn={Boolean(authUser)}
            dashboard={dashboard}
            nextClass={nextClass}
            reservations={reservations}
            selectedPlan={selectedPlan}
            ranking={ranking}
            onReserve={reserveClass}
            onStartWorkout={startWorkout}
            onCheckIn={checkIn}
            onNavigate={setActiveView}
            onRegister={() => openAuth('register')}
          />
        )}
        {activeView === 'classes' && (
          <ClassesView
            classes={normalizedClasses}
            activeDate={activeDate}
            reservations={reservations}
            onDateChange={setActiveDate}
            onReserve={reserveClass}
          />
        )}
        {activeView === 'trainers' && (
          <TrainersView trainers={trainers} onCreatePlan={createTrainingPlan} />
        )}
        {activeView === 'plans' && (
          <PlansView plans={fallbackPlans} selectedPlan={selectedPlan} onChoosePlan={choosePlan} />
        )}
        {activeView === 'journal' && (
          <JournalView
            exercises={exercises}
            session={session}
            selectedExercise={selectedExercise}
            entries={trainingEntries}
            onSelectExercise={setSelectedExercise}
            onStartWorkout={startWorkout}
            onCreatePlan={createTrainingPlan}
            onAddEntry={addTrainingEntry}
          />
        )}
        {activeView === 'records' && (
          <RecordsView
            records={records}
            ranking={ranking}
            selectedExercise={selectedExercise}
            reservations={reservations}
            exercises={exercises}
            onAddRecord={addRecord}
          />
        )}
        {activeView === 'profile' && (
          <ProfileView user={user} isLoggedIn={Boolean(authUser)} selectedPlan={selectedPlan} reservations={reservations} onCheckIn={checkIn} onLogout={logout} onRegister={() => openAuth('register')} />
        )}
      </main>

      {authOpen && (
        <AuthModal
          mode={authMode}
          onClose={() => setAuthOpen(false)}
          onModeChange={setAuthMode}
          onSubmit={submitAuth}
        />
      )}

      {paymentOpen && (
        <PaymentDialog
          plan={paymentDraft}
          onClose={() => setPaymentOpen(false)}
          onConfirm={confirmPayment}
        />
      )}

      {searchOpen && (
        <SearchDialog
          classes={normalizedClasses}
          exercises={exercises}
          onClose={() => setSearchOpen(false)}
          onGo={(view) => {
            setActiveView(view);
            setSearchOpen(false);
          }}
        />
      )}

      {notificationsOpen && (
        <NotificationsDialog
          reservations={reservations}
          selectedPlan={selectedPlan}
          session={session}
          onClose={() => setNotificationsOpen(false)}
        />
      )}
    </div>
  );
}

function normalizeClasses(items) {
  return items.map((item, index) => {
    const startsAt = item.startsAt ?? item.time ?? '2026-05-25T18:00:00';
    const startsAtText = String(startsAt);
    const date = startsAtText.slice(0, 10).includes('-') ? startsAtText.slice(0, 10) : `2026-05-${25 + index}`;
    const time = startsAtText.includes('T') ? startsAtText.slice(11, 16) : startsAtText.replace('Dziś ', '').replace('Jutro ', '');
    return {
      id: item.id ?? `${item.name}-${date}-${time}`,
      name: item.name,
      trainerId: item.trainerId,
      date,
      time,
      capacity: item.capacity ?? Number.parseInt(item.places, 10) ?? 12,
      level: item.level ?? 'średni'
    };
  });
}

function PaymentDialog({ plan, onClose, onConfirm }) {
  const [method, setMethod] = useState('BLIK demo');
  const methods = ['BLIK demo', 'Przelew demo', 'Portfel klubowy'];

  return (
    <Modal title="Płatność demo" subtitle="To tylko symulacja płatności. Nie wpisujesz karty ani prawdziwych danych." onClose={onClose}>
      <div className="paymentSummary">
        <CreditCard size={22} />
        <span>Karnet <b>{plan?.planName}</b><small>{plan?.price}/mies.</small></span>
      </div>
      <div className="paymentGrid">
        {methods.map((item) => (
          <button key={item} className={method === item ? 'active' : ''} type="button" onClick={() => setMethod(item)}>
            {item}
          </button>
        ))}
      </div>
      <button className="primary fullWidth" type="button" onClick={() => onConfirm(method)}>Potwierdź płatność demo</button>
    </Modal>
  );
}

function SearchDialog({ classes, exercises, onClose, onGo }) {
  const [query, setQuery] = useState('');
  const normalized = query.trim().toLowerCase();
  const results = [
    ...classes
      .filter((item) => item.name.toLowerCase().includes(normalized))
      .map((item) => ({ label: item.name, meta: `${item.date}, ${item.time}`, view: 'classes' })),
    ...exercises
      .filter((item) => (item.name ?? item.Name).toLowerCase().includes(normalized))
      .map((item) => ({ label: item.name ?? item.Name, meta: item.category ?? item.focus ?? 'Ćwiczenie', view: 'journal' }))
  ];

  return (
    <Modal title="Szybkie wyszukiwanie" subtitle="Znajdź zajęcia, ćwiczenie albo przejdź do odpowiedniego widoku." onClose={onClose}>
      <label className="searchBox">
        <Search size={18} />
        <input autoFocus value={query} onChange={(event) => setQuery(event.target.value)} placeholder="np. Power, martwy, trener" />
      </label>
      <div className="resultList">
        {(normalized ? results : []).map((result) => (
          <button key={`${result.view}-${result.label}-${result.meta}`} type="button" onClick={() => onGo(result.view)}>
            <b>{result.label}</b>
            <span>{result.meta}</span>
          </button>
        ))}
        {normalized && results.length === 0 && <p>Brak wyników dla tej frazy.</p>}
      </div>
    </Modal>
  );
}

function NotificationsDialog({ reservations, selectedPlan, session, onClose }) {
  return (
    <Modal title="Powiadomienia" subtitle="Najważniejsze informacje z panelu klienta." onClose={onClose}>
      <div className="notificationStack">
        <article>
          <b>Karnet {selectedPlan}</b>
          <span>Aktywny plan jest widoczny w profilu i panelu.</span>
        </article>
        <article>
          <b>{reservations.length ? `${reservations.length} rezerwacje` : 'Brak rezerwacji'}</b>
          <span>{reservations.length ? reservations.map((item) => `${item.name} (${item.date})`).join(', ') : 'Wybierz zajęcia z kalendarza.'}</span>
        </article>
        <article>
          <b>{session ? 'Trening rozpoczęty' : 'Trening czeka'}</b>
          <span>{session?.summary ?? 'Możesz rozpocząć sesję z panelu lub dziennika.'}</span>
        </article>
      </div>
      <button className="primary fullWidth" type="button" onClick={onClose}>Oznacz jako przeczytane</button>
    </Modal>
  );
}
