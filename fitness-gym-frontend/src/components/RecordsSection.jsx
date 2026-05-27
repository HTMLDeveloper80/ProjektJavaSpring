import { BarChart3, Trophy } from 'lucide-react';

export function RecordsSection({ selectedExercise, selectedPlan, reservationsCount }) {
  return (
    <section className="panel recordsPanel" id="Rekordy">
      <div className="sectionHead">
        <span>Rekordy osobiste</span>
        <h2>Twoje top wyniki</h2>
      </div>
      <div className="recordGrid">
        <div>
          <Trophy />
          <b>{selectedExercise.record} kg</b>
          <span>{selectedExercise.name}</span>
        </div>
        <div>
          <BarChart3 />
          <b>{selectedExercise.trend}</b>
          <span>progres w ostatnim cyklu</span>
        </div>
        <div>
          <b>{selectedPlan}</b>
          <span>aktywny karnet</span>
        </div>
        <div>
          <b>{reservationsCount}</b>
          <span>rezerwacje w tygodniu</span>
        </div>
      </div>
    </section>
  );
}
