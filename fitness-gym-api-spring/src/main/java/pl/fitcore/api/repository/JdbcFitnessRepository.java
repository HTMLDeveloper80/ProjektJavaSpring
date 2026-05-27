package pl.fitcore.api.repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.fitcore.backend.domain.CheckIn;
import pl.fitcore.backend.domain.GymClass;
import pl.fitcore.backend.domain.Member;
import pl.fitcore.backend.domain.Membership;
import pl.fitcore.backend.domain.MembershipType;
import pl.fitcore.backend.domain.Reservation;
import pl.fitcore.backend.domain.Trainer;
import pl.fitcore.backend.repository.FitnessRepository;

@Repository
public class JdbcFitnessRepository implements FitnessRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcFitnessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveMember(Member member) {
        jdbcTemplate.update(
            "MERGE INTO members (id, name, email, qr_code) KEY(id) VALUES (?, ?, ?, ?)",
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getQrCode()
        );
    }

    public void saveMembership(Membership membership) {
        jdbcTemplate.update("DELETE FROM memberships WHERE member_id = ?", membership.getMemberId());
        jdbcTemplate.update(
            "INSERT INTO memberships (id, member_id, type, valid_from, valid_to) VALUES (?, ?, ?, ?, ?)",
            membership.getId(),
            membership.getMemberId(),
            membership.getType().name(),
            Date.valueOf(membership.getValidFrom()),
            Date.valueOf(membership.getValidTo())
        );
    }

    public void saveTrainer(Trainer trainer) {
        jdbcTemplate.update(
            "MERGE INTO trainers (id, name, specialization) KEY(id) VALUES (?, ?, ?)",
            trainer.getId(),
            trainer.getName(),
            trainer.getSpecialization()
        );
    }

    public void saveClass(GymClass gymClass) {
        jdbcTemplate.update(
            "MERGE INTO gym_classes (id, name, trainer_id, starts_at, capacity, level) KEY(id) VALUES (?, ?, ?, ?, ?, ?)",
            gymClass.getId(),
            gymClass.getName(),
            gymClass.getTrainerId(),
            Timestamp.valueOf(gymClass.getStartsAt()),
            gymClass.getCapacity(),
            gymClass.getLevel()
        );
    }

    public void saveReservation(Reservation reservation) {
        jdbcTemplate.update(
            "MERGE INTO reservations (id, member_id, class_id, created_at, status) KEY(id) VALUES (?, ?, ?, ?, 'ACTIVE')",
            "R-" + reservation.getMemberId() + "-" + reservation.getClassId(),
            reservation.getMemberId(),
            reservation.getClassId(),
            Timestamp.valueOf(reservation.getCreatedAt())
        );
    }

    public void saveCheckIn(CheckIn checkIn) {
        jdbcTemplate.update(
            "INSERT INTO check_ins (id, member_id, checked_in_at) VALUES (?, ?, ?)",
            "CI-" + checkIn.getMemberId() + "-" + System.nanoTime(),
            checkIn.getMemberId(),
            Timestamp.valueOf(checkIn.getCheckedInAt())
        );
    }

    public Optional<Member> findMemberById(String memberId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                "SELECT id, name, email, qr_code FROM members WHERE id = ?",
                this::mapMember,
                memberId
            ));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Membership> findActiveMembershipForMember(String memberId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                "SELECT id, member_id, type, valid_from, valid_to FROM memberships WHERE member_id = ? AND CURRENT_DATE BETWEEN valid_from AND valid_to ORDER BY valid_to DESC LIMIT 1",
                this::mapMembership,
                memberId
            ));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<GymClass> findClassById(String classId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                "SELECT id, name, trainer_id, starts_at, capacity, level FROM gym_classes WHERE id = ?",
                this::mapClass,
                classId
            ));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean hasReservation(String memberId, String classId) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM reservations WHERE member_id = ? AND class_id = ? AND status = 'ACTIVE'",
            Integer.class,
            memberId,
            classId
        );
        return count != null && count > 0;
    }

    public List<Reservation> findReservationsForMember(String memberId) {
        return jdbcTemplate.query(
            "SELECT member_id, class_id, created_at FROM reservations WHERE member_id = ? AND status = 'ACTIVE' ORDER BY created_at DESC",
            this::mapReservation,
            memberId
        );
    }

    public int countReservationsForClass(String classId) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM reservations WHERE class_id = ? AND status = 'ACTIVE'",
            Integer.class,
            classId
        );
        return count == null ? 0 : count;
    }

    public List<Member> findAllMembers() {
        return jdbcTemplate.query("SELECT id, name, email, qr_code FROM members ORDER BY id", this::mapMember);
    }

    public List<GymClass> findAllClasses() {
        return jdbcTemplate.query(
            "SELECT id, name, trainer_id, starts_at, capacity, level FROM gym_classes ORDER BY starts_at",
            this::mapClass
        );
    }

    public List<Trainer> findAllTrainers() {
        return jdbcTemplate.query("SELECT id, name, specialization FROM trainers ORDER BY name", this::mapTrainer);
    }

    private Member mapMember(ResultSet resultSet, int rowNum) throws SQLException {
        return new Member(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("qr_code")
        );
    }

    private Membership mapMembership(ResultSet resultSet, int rowNum) throws SQLException {
        return new Membership(
            resultSet.getString("id"),
            resultSet.getString("member_id"),
            MembershipType.valueOf(resultSet.getString("type")),
            resultSet.getDate("valid_from").toLocalDate(),
            resultSet.getDate("valid_to").toLocalDate()
        );
    }

    private Trainer mapTrainer(ResultSet resultSet, int rowNum) throws SQLException {
        return new Trainer(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("specialization")
        );
    }

    private GymClass mapClass(ResultSet resultSet, int rowNum) throws SQLException {
        LocalDateTime startsAt = resultSet.getTimestamp("starts_at").toLocalDateTime();
        return new GymClass(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("trainer_id"),
            startsAt,
            resultSet.getInt("capacity"),
            resultSet.getString("level")
        );
    }

    private Reservation mapReservation(ResultSet resultSet, int rowNum) throws SQLException {
        return new Reservation(
            resultSet.getString("member_id"),
            resultSet.getString("class_id"),
            resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
