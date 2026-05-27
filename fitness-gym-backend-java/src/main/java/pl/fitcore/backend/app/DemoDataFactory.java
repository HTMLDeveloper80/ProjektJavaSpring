package pl.fitcore.backend.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import pl.fitcore.backend.domain.GymClass;
import pl.fitcore.backend.domain.Member;
import pl.fitcore.backend.domain.Membership;
import pl.fitcore.backend.domain.MembershipType;
import pl.fitcore.backend.domain.Trainer;
import pl.fitcore.backend.integration.DemoTrainingModuleClient;
import pl.fitcore.backend.integration.TrainingModuleClient;
import pl.fitcore.backend.repository.InMemoryFitnessRepository;

public final class DemoDataFactory {
    private DemoDataFactory() {
    }

    public static FitnessBackendService createService() {
        return createService(new DemoTrainingModuleClient());
    }

    public static FitnessBackendService createService(TrainingModuleClient trainingModuleClient) {
        InMemoryFitnessRepository repository = new InMemoryFitnessRepository();

        repository.saveMember(new Member("M-1001", "Klubowicz Demo", "demo@fitcore.local", "QR-M-1001"));
        repository.saveMembership(new Membership("MS-1001", "M-1001", MembershipType.PRO, LocalDate.now().minusDays(4), LocalDate.now().plusMonths(1)));

        repository.saveTrainer(new Trainer("T-ADAM", "Adam Nowak", "siła, redukcja, technika bojów"));
        repository.saveClass(new GymClass("C-POWER-PUMP", "Power Pump", "T-ADAM", LocalDateTime.now().plusHours(2), 12, "średni"));
        repository.saveClass(new GymClass("C-MOBILITY", "Mobility Flow", "T-ADAM", LocalDateTime.now().plusDays(1), 16, "lekki"));
        repository.saveClass(new GymClass("C-BOX-FIT", "Box Fit", "T-ADAM", LocalDateTime.now().plusDays(1).plusHours(9), 10, "mocny"));

        return new FitnessBackendService(repository, trainingModuleClient);
    }
}
