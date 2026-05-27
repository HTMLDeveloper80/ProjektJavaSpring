import { CheckCircle2 } from 'lucide-react';

export function PlansSection({ plans, selectedPlan, onChoosePlan }) {
  return (
    <section className="plans" id="Karnety">
      <div className="sectionHead center">
        <span>Karnety</span>
        <h2>Wybierz plan dla siebie</h2>
      </div>
      <div className="planGrid">
        {plans.map((plan) => {
          const selected = selectedPlan === plan.name;

          return (
            <article className={`plan ${plan.hot ? 'hot' : ''} ${selected ? 'selectedPlan' : ''}`} key={plan.name}>
              <span className="tag">{selected ? 'Aktywny wybór' : plan.tag}</span>
              <h3>{plan.name}</h3>
              <div className="price">{plan.price}<small>/ mies.</small></div>
              {plan.perks.map((perk) => (
                <p key={perk}><CheckCircle2 size={17} />{perk}</p>
              ))}
              <button type="button" onClick={() => onChoosePlan(plan.name)}>
                {selected ? 'Wybrany' : plan.hot ? 'Wybierz Pro' : 'Sprawdź'}
              </button>
            </article>
          );
        })}
      </div>
    </section>
  );
}
