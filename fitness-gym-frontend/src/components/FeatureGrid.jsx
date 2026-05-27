import { features } from '../data/content.js';

export function FeatureGrid() {
  return (
    <section className="features" aria-label="Funkcje aplikacji">
      {features.map(({ icon: Icon, title, text }) => (
        <article className="feature" key={title}>
          <div><Icon size={24} /></div>
          <h3>{title}</h3>
          <p>{text}</p>
        </article>
      ))}
    </section>
  );
}
