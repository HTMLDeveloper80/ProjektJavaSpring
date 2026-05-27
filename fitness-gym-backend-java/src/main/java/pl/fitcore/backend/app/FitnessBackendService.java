package pl.fitcore.backend.app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import pl.fitcore.backend.domain.CheckIn;
import pl.fitcore.backend.domain.CheckInResult;
import pl.fitcore.backend.domain.GymClass;
import pl.fitcore.backend.domain.Member;
import pl.fitcore.backend.domain.Membership;
import pl.fitcore.backend.domain.MembershipType;
import pl.fitcore.backend.domain.Reservation;
import pl.fitcore.backend.domain.ReservationResult;
import pl.fitcore.backend.domain.Trainer;
import pl.fitcore.backend.domain.TrainingPlanSummary;
import pl.fitcore.backend.domain.WorkoutSessionSummary;
import pl.fitcore.backend.integration.TrainingModuleClient;
import pl.fitcore.backend.repository.FitnessRepository;

public class FitnessBackendService {
    private final FitnessRepository repository;
    private final TrainingModuleClient trainingModuleClient;

    public FitnessBackendService(FitnessRepository repository, TrainingModuleClient trainingModuleClient) {
        this.repository = repository;
        this.trainingModuleClient = trainingModuleClient;
    }

    public Member registerMember(String name, String email) {
        String id = "M-" + (1000 + repository.findAllMembers().size() + 1);
        Member member = new Member(id, name, email, "QR-" + id);
        repository.saveMember(member);
        repository.saveMembership(Membership.starting(id));
        return member;
    }

    public Membership selectMembership(String memberId, MembershipType type) {
        requireMember(memberId);
        Membership membership = Membership.monthly(memberId, type);
        repository.saveMembership(membership);
        return membership;
    }

    public CheckInResult checkIn(String memberId, String scannedQrCode) {
        Member member = requireMember(memberId);
        Optional<Membership> membership = repository.findActiveMembershipForMember(memberId);

        if (!member.getQrCode().equals(scannedQrCode)) {
            return CheckInResult.rejected("Kod QR nie pasuje do konta członka.");
        }

        if (!membership.isPresent() || !membership.get().isActive()) {
            return CheckInResult.rejected("Brak aktywnego karnetu.");
        }

        repository.saveCheckIn(new CheckIn(memberId, LocalDateTime.now()));
        WorkoutSessionSummary session = trainingModuleClient.startSession(memberId);
        return CheckInResult.accepted("Wejście potwierdzone. " + session.getSummary());
    }

    public ReservationResult reserveClass(String memberId, String classId) {
        requireMember(memberId);
        GymClass gymClass = repository.findClassById(classId)
            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zajęć: " + classId));

        if (!repository.findActiveMembershipForMember(memberId).isPresent()) {
            return ReservationResult.rejected("Nie można zapisać na zajęcia bez aktywnego karnetu.");
        }

        int reservedPlaces = repository.countReservationsForClass(classId);
        if (reservedPlaces >= gymClass.getCapacity()) {
            return ReservationResult.rejected("Brak wolnych miejsc na zajęciach " + gymClass.getName() + ".");
        }

        if (repository.hasReservation(memberId, classId)) {
            return ReservationResult.accepted("Członek jest już zapisany na " + gymClass.getName() + ".");
        }

        repository.saveReservation(new Reservation(memberId, classId, LocalDateTime.now()));
        return ReservationResult.accepted("Zapisano na zajęcia " + gymClass.getName() + ".");
    }

    public List<GymClass> reservationsForMember(String memberId) {
        requireMember(memberId);
        List<GymClass> reservedClasses = new java.util.ArrayList<>();
        for (Reservation reservation : repository.findReservationsForMember(memberId)) {
            repository.findClassById(reservation.getClassId()).ifPresent(reservedClasses::add);
        }
        return reservedClasses;
    }

    public WorkoutSessionSummary startWorkoutSession(String memberId) {
        requireMember(memberId);
        return trainingModuleClient.startSession(memberId);
    }

    public TrainingPlanSummary assignTrainingPlan(String memberId, String goal, String level) {
        requireMember(memberId);
        return trainingModuleClient.createTrainingPlan(memberId, goal, level);
    }

    public List<Member> members() {
        return repository.findAllMembers();
    }

    public List<GymClass> classes() {
        return repository.findAllClasses();
    }

    public List<Trainer> trainers() {
        return repository.findAllTrainers();
    }

    private Member requireMember(String memberId) {
        return repository.findMemberById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono członka: " + memberId));
    }
}
