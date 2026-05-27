import {
  Activity,
  BarChart3,
  CalendarDays,
  Dumbbell,
  Flame,
  ShieldCheck,
  Trophy,
  Users
} from 'lucide-react';

export const navItems = ['Start', 'Zajęcia', 'Karnety', 'Trenerzy', 'Dziennik', 'Rekordy'];

export const classes = [
  { id: 'LOCAL-MON-POWER', name: 'Power Pump', startsAt: '2026-05-25T18:00:00', capacity: 8, level: 'średni', icon: Flame },
  { id: 'LOCAL-MON-CORE', name: 'Core Stability', startsAt: '2026-05-25T19:15:00', capacity: 12, level: 'lekki', icon: Activity },
  { id: 'LOCAL-TUE-MOBILITY', name: 'Mobility Flow', startsAt: '2026-05-26T09:30:00', capacity: 14, level: 'lekki', icon: Activity },
  { id: 'LOCAL-TUE-BOX', name: 'Box Fit', startsAt: '2026-05-26T19:00:00', capacity: 10, level: 'mocny', icon: Dumbbell },
  { id: 'LOCAL-WED-STRENGTH', name: 'Full Body Strength', startsAt: '2026-05-27T17:30:00', capacity: 12, level: 'średni', icon: Dumbbell },
  { id: 'LOCAL-FRI-HIIT', name: 'HIIT Circuit', startsAt: '2026-05-29T19:00:00', capacity: 12, level: 'mocny', icon: Flame }
];

export const plans = [
  {
    name: 'Start',
    price: '99 zł',
    tag: 'Dla początkujących',
    perks: ['Siłownia open', 'Biblioteka ćwiczeń', 'Podstawowy dziennik']
  },
  {
    name: 'Pro',
    price: '149 zł',
    tag: 'Najczęściej wybierany',
    perks: ['Zajęcia grupowe', 'Rezerwacje online', 'Rekordy osobiste', 'Statystyki postępów'],
    hot: true
  },
  {
    name: 'Elite',
    price: '229 zł',
    tag: 'Pełna opieka',
    perks: ['Plan od trenera', 'Analiza wyników', 'Priorytet rezerwacji', 'Konsultacje']
  }
];

export const features = [
  {
    icon: Users,
    title: 'Członkowie i karnety',
    text: 'Profile klientów, status opłat, historia wejść i szybkie przedłużanie abonamentu.'
  },
  {
    icon: CalendarDays,
    title: 'Zajęcia i rezerwacje',
    text: 'Grafik zajęć grupowych, limity miejsc, lista zapisów i odwoływanie rezerwacji.'
  },
  {
    icon: Dumbbell,
    title: 'Plany treningowe',
    text: 'Biblioteka ćwiczeń, gotowe plany, serie, powtórzenia, ciężary i notatki.'
  },
  {
    icon: BarChart3,
    title: 'Dziennik i progres',
    text: 'Monitorowanie sesji treningowych, wykresy postępów oraz rekordy osobiste.'
  }
];

export const exercises = [
  { id: 'EX-SQUAT', name: 'Przysiad ze sztangą', record: 140, volume: '4 serie x 6', trend: '+12 kg', focus: 'Nogi', difficulty: 'średni', muscles: ['nogi', 'pośladki', 'core'], icon: Trophy },
  { id: 'EX-BENCH', name: 'Wyciskanie na ławce', record: 105, volume: '5 serii x 5', trend: '+7 kg', focus: 'Klatka', difficulty: 'średni', muscles: ['klatka', 'triceps', 'barki'], icon: ShieldCheck },
  { id: 'EX-DEADLIFT', name: 'Martwy ciąg', record: 180, volume: '3 serie x 4', trend: '+15 kg', focus: 'Siła', difficulty: 'mocny', muscles: ['plecy', 'nogi', 'core'], icon: Dumbbell },
  { id: 'EX-PULLUP', name: 'Podciąganie', record: 22, volume: '4 serie', trend: '+4 powt.', focus: 'Plecy', difficulty: 'średni', muscles: ['plecy', 'biceps'], icon: Activity },
  { id: 'EX-HIP-THRUST', name: 'Hip thrust', record: 160, volume: '4 serie x 8', trend: '+20 kg', focus: 'Pośladki', difficulty: 'lekki', muscles: ['pośladki', 'dwugłowe uda'], icon: Flame }
];

export const weeklyStats = [
  { value: '2 480', label: 'aktywnych klubowiczów' },
  { value: '64', label: 'zajęcia tygodniowo' },
  { value: '98%', label: 'pozytywnych opinii' }
];
