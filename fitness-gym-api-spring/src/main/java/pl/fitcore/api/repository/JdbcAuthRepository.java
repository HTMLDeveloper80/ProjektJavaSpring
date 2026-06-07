package pl.fitcore.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.fitcore.backend.domain.Member;

@Repository
public class JdbcAuthRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findMemberByEmail(String email) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                "SELECT id, name, email, qr_code FROM members WHERE LOWER(email) = LOWER(?)",
                this::mapMember,
                email
            ));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
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

    public Optional<String> findPasswordHash(String memberId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT password_hash FROM auth_credentials WHERE member_id = ?",
                String.class,
                memberId
            ));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void savePassword(String memberId, String passwordHash) {
        jdbcTemplate.update(
            "MERGE INTO auth_credentials (member_id, password_hash, updated_at) KEY(member_id) VALUES (?, ?, CURRENT_TIMESTAMP)",
            memberId,
            passwordHash
        );
    }

    public void saveRefreshToken(String tokenId, String memberId, String tokenHash, Instant expiresAt) {
        jdbcTemplate.update(
            "INSERT INTO refresh_tokens (id, member_id, token_hash, expires_at) VALUES (?, ?, ?, ?)",
            tokenId,
            memberId,
            tokenHash,
            Timestamp.from(expiresAt)
        );
    }

    public boolean consumeRefreshToken(String tokenHash, String memberId, Instant now) {
        int updated = jdbcTemplate.update(
            "UPDATE refresh_tokens SET revoked = TRUE WHERE token_hash = ? AND member_id = ? AND revoked = FALSE AND expires_at > ?",
            tokenHash,
            memberId,
            Timestamp.from(now)
        );
        return updated == 1;
    }

    public void revokeRefreshToken(String tokenHash) {
        jdbcTemplate.update(
            "UPDATE refresh_tokens SET revoked = TRUE WHERE token_hash = ?",
            tokenHash
        );
    }

    private Member mapMember(ResultSet resultSet, int rowNum) throws SQLException {
        return new Member(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("qr_code")
        );
    }
}
