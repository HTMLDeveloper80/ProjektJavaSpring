import { ChevronRight } from 'lucide-react';

export function ExerciseLibrary({ exercises, selectedExercise, onSelectExercise }) {
  return (
    <section className="library panel" id="Dziennik">
      <div className="sectionHead">
        <span>Biblioteka ćwiczeń</span>
        <h2>Ćwiczenia i rekordy</h2>
      </div>
      <div className="exerciseLayout">
        <div className="exerciseList">
          {exercises.map((exercise, index) => (
            <button
              className={`exercise ${selectedExercise.name === exercise.name ? 'active' : ''}`}
              type="button"
              key={exercise.name}
              onClick={() => onSelectExercise(exercise)}
            >
              <span>0{index + 1}</span>
              <b>{exercise.name}</b>
              <small>Rekord: {exercise.record} kg</small>
              <ChevronRight size={18} />
            </button>
          ))}
        </div>

        <article className="exerciseDetails">
          <span className="pill">{selectedExercise.focus}</span>
          <h3>{selectedExercise.name}</h3>
          <dl>
            <div>
              <dt>Rekord</dt>
              <dd>{selectedExercise.record} kg</dd>
            </div>
            <div>
              <dt>Ostatnia sesja</dt>
              <dd>{selectedExercise.volume}</dd>
            </div>
            <div>
              <dt>Trend</dt>
              <dd>{selectedExercise.trend}</dd>
            </div>
          </dl>
        </article>
      </div>
    </section>
  );
}
