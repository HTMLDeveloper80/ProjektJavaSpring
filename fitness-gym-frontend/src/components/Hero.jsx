import { ArrowRight, Clock, PlayCircle, ShieldCheck, Trophy } from 'lucide-react';
import { weeklyStats } from '../data/content.js';

export function Hero({ nextClass, startedWorkout, reservationsCount, onReserve, onDemo, onStartWorkout }) {
  return (
    <section className="hero" id="Start">
      <div className="heroText">
        <span className="eyebrow"><ShieldCheck size={16} /> Aplikacja fitness i siłownia</span>
        <h1>Trening, karnety i rezerwacje w jednym nowoczesnym panelu.</h1>
        <p>
          Panel do zarządzania członkami, karnetami, zajęciami grupowymi, harmonogramem trenerów,
          planami treningowymi, dziennikiem sesji i rekordami osobistymi.
        </p>
        <div className="heroButtons">
          <button className="primary" type="button" onClick={onReserve}>
            Zarezerwuj {nextClass.name}
            <ArrowRight size={18} />
          </button>
          <button className="secondary" type="button" onClick={onDemo}>
            <PlayCircle size={18} />
            Zobacz demo
          </button>
        </div>
        <div className="stats">
          {weeklyStats.map((stat) => (
            <div key={stat.label}>
              <b>{stat.value}</b>
              <span>{stat.label}</span>
            </div>
          ))}
        </div>
      </div>

      <aside className="dashboardCard" aria-label="Plan aktywności">
        <div className="cardHeader">
          <div>
            <span>Twój tydzień</span>
            <h3>Plan aktywności</h3>
          </div>
          <span className="pill">{startedWorkout ? 'Trwa' : 'Live'}</span>
        </div>
        <div className="progressRing">
          <div>
            <b>{startedWorkout ? '82%' : '76%'}</b>
            <span>celu</span>
          </div>
        </div>
        <div className="miniGrid">
          <div>
            <Clock />
            <b>{startedWorkout ? '6h 10m' : '5h 40m'}</b>
            <span>czas treningu</span>
          </div>
          <div>
            <Trophy />
            <b>{12 + reservationsCount}</b>
            <span>nowe rekordy</span>
          </div>
        </div>
        <div className="session">
          <div>
            <span className="dot"></span>
            <div>
              <b>Push Day</b>
              <small>Dziś, 17:30 - trener Adam</small>
            </div>
          </div>
          <button type="button" onClick={onStartWorkout}>{startedWorkout ? 'W toku' : 'Start'}</button>
        </div>
      </aside>
    </section>
  );
}
