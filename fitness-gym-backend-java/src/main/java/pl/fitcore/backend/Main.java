package pl.fitcore.backend;

import pl.fitcore.backend.app.DemoDataFactory;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.CheckInResult;
import pl.fitcore.backend.domain.ReservationResult;
import pl.fitcore.backend.domain.TrainingPlanSummary;
import pl.fitcore.backend.domain.WorkoutSessionSummary;

public class Main {
    public static void main(String[] args) {
        FitnessBackendService service = DemoDataFactory.createService();

        CheckInResult checkIn = service.checkIn("M-1001", "QR-M-1001");
        ReservationResult reservation = service.reserveClass("M-1001", "C-POWER-PUMP");
        WorkoutSessionSummary session = service.startWorkoutSession("M-1001");
        TrainingPlanSummary plan = service.assignTrainingPlan("M-1001", "siła i redukcja", "średni");

        System.out.println(checkIn.getMessage());
        System.out.println(reservation.getMessage());
        System.out.println(session.getSummary());
        System.out.println(plan.getTitle());
    }
}
