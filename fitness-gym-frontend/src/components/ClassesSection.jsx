import { Star } from 'lucide-react';

export function ClassesSection({ classes, reservations, onReserve }) {
  return (
    <section className="split" id="Zajęcia">
      <div className="panel classesPanel">
        <div className="sectionHead">
          <span>Zajęcia grupowe</span>
          <h2>Najbliższe rezerwacje</h2>
        </div>
        {classes.map((item) => {
          const reserved = reservations.includes(item.name);
          const Icon = item.icon;

          return (
            <div className="classRow" key={item.name}>
              <div className="classIcon"><Icon size={20} /></div>
              <div>
                <b>{item.name}</b>
                <small>{item.time} - {item.level}</small>
              </div>
              <button
                className={`chipBtn ${reserved ? 'selected' : ''}`}
                type="button"
                onClick={() => onReserve(item.name)}
              >
                {reserved ? 'Zapisano' : `${item.places} miejsc`}
              </button>
            </div>
          );
        })}
      </div>

      <div className="panel trainerPanel" id="Trenerzy">
        <div className="trainerTop">
          <img
            src="https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?auto=format&fit=crop&w=600&q=80"
            alt="Trener Adam Nowak"
          />
          <div>
            <span>Trener tygodnia</span>
            <h2>Adam Nowak</h2>
            <p>Specjalizacja: siła, redukcja, technika bojów. Wolne terminy pojawiają się po wybraniu karnetu Pro lub Elite.</p>
          </div>
        </div>
        <div className="rating">
          <Star fill="currentColor" />
          <b>4.9</b>
          <span>312 opinii</span>
        </div>
      </div>
    </section>
  );
}
