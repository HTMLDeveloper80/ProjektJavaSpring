package pl.fitcore.backend.repository;

import java.util.List;
import java.util.Optional;
import pl.fitcore.backend.domain.CheckIn;
import pl.fitcore.backend.domain.GymClass;
import pl.fitcore.backend.domain.Member;
import pl.fitcore.backend.domain.Membership;
import pl.fitcore.backend.domain.Reservation;
import pl.fitcore.backend.domain.Trainer;

public interface FitnessRepository {
    void saveMember(Member member);

    void saveMembership(Membership membership);

    void saveTrainer(Trainer trainer);

    void saveClass(GymClass gymClass);

    void saveReservation(Reservation reservation);

    void saveCheckIn(CheckIn checkIn);

    Optional<Member> findMemberById(String memberId);

    Optional<Membership> findActiveMembershipForMember(String memberId);

    Optional<GymClass> findClassById(String classId);

    boolean hasReservation(String memberId, String classId);

    List<Reservation> findReservationsForMember(String memberId);

    int countReservationsForClass(String classId);

    List<Member> findAllMembers();

    List<GymClass> findAllClasses();

    List<Trainer> findAllTrainers();
}
