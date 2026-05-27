package pl.fitcore.backend.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pl.fitcore.backend.domain.CheckIn;
import pl.fitcore.backend.domain.GymClass;
import pl.fitcore.backend.domain.Member;
import pl.fitcore.backend.domain.Membership;
import pl.fitcore.backend.domain.Reservation;
import pl.fitcore.backend.domain.Trainer;

public class InMemoryFitnessRepository implements FitnessRepository {
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final Map<String, Membership> memberships = new LinkedHashMap<>();
    private final Map<String, GymClass> classes = new LinkedHashMap<>();
    private final Map<String, Trainer> trainers = new LinkedHashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<CheckIn> checkIns = new ArrayList<>();

    public void saveMember(Member member) {
        members.put(member.getId(), member);
    }

    public void saveMembership(Membership membership) {
        memberships.put(membership.getMemberId(), membership);
    }

    public void saveTrainer(Trainer trainer) {
        trainers.put(trainer.getId(), trainer);
    }

    public void saveClass(GymClass gymClass) {
        classes.put(gymClass.getId(), gymClass);
    }

    public void saveReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void saveCheckIn(CheckIn checkIn) {
        checkIns.add(checkIn);
    }

    public Optional<Member> findMemberById(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    public Optional<Membership> findActiveMembershipForMember(String memberId) {
        Membership membership = memberships.get(memberId);
        if (membership == null || !membership.isActive()) {
            return Optional.empty();
        }
        return Optional.of(membership);
    }

    public Optional<GymClass> findClassById(String classId) {
        return Optional.ofNullable(classes.get(classId));
    }

    public boolean hasReservation(String memberId, String classId) {
        return reservations.stream()
            .anyMatch(reservation -> reservation.getMemberId().equals(memberId) && reservation.getClassId().equals(classId));
    }

    public List<Reservation> findReservationsForMember(String memberId) {
        List<Reservation> memberReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getMemberId().equals(memberId)) {
                memberReservations.add(reservation);
            }
        }
        return memberReservations;
    }

    public int countReservationsForClass(String classId) {
        return (int) reservations.stream()
            .filter(reservation -> reservation.getClassId().equals(classId))
            .count();
    }

    public List<Member> findAllMembers() {
        return new ArrayList<>(members.values());
    }

    public List<GymClass> findAllClasses() {
        return new ArrayList<>(classes.values());
    }

    public List<Trainer> findAllTrainers() {
        return new ArrayList<>(trainers.values());
    }
}
