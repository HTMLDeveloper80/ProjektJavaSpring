-- Aktywny karnet członka
SELECT *
FROM memberships
WHERE member_id = ?
  AND CURRENT_DATE BETWEEN valid_from AND valid_to
ORDER BY valid_to DESC;

-- Najbliższe zajęcia z liczbą zajętych miejsc
SELECT
    c.id,
    c.name,
    c.starts_at,
    c.capacity,
    c.level,
    t.name AS trainer_name,
    COUNT(r.id) AS reserved_places
FROM gym_classes c
JOIN trainers t ON t.id = c.trainer_id
LEFT JOIN reservations r ON r.class_id = c.id AND r.status = 'ACTIVE'
WHERE c.starts_at >= CURRENT_TIMESTAMP
GROUP BY c.id, c.name, c.starts_at, c.capacity, c.level, t.name
ORDER BY c.starts_at;

-- Rezerwacje członka
SELECT
    r.id,
    r.status,
    r.created_at,
    c.name AS class_name,
    c.starts_at,
    t.name AS trainer_name
FROM reservations r
JOIN gym_classes c ON c.id = r.class_id
JOIN trainers t ON t.id = c.trainer_id
WHERE r.member_id = ?
ORDER BY c.starts_at;

-- Historia wejść na siłownię
SELECT
    checked_in_at,
    training_session_id
FROM check_ins
WHERE member_id = ?
ORDER BY checked_in_at DESC;
