CREATE TABLE members (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    qr_code VARCHAR(80) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE memberships (
    id VARCHAR(64) PRIMARY KEY,
    member_id VARCHAR(32) NOT NULL,
    type VARCHAR(20) NOT NULL,
    valid_from DATE NOT NULL,
    valid_to DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_memberships_member
        FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT chk_membership_type
        CHECK (type IN ('START', 'PRO', 'ELITE')),
    CONSTRAINT chk_membership_dates
        CHECK (valid_to >= valid_from)
);

CREATE TABLE trainers (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    specialization VARCHAR(255) NOT NULL
);

CREATE TABLE gym_classes (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    trainer_id VARCHAR(32) NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    capacity INT NOT NULL,
    level VARCHAR(40) NOT NULL,
    CONSTRAINT fk_gym_classes_trainer
        FOREIGN KEY (trainer_id) REFERENCES trainers(id),
    CONSTRAINT chk_gym_classes_capacity
        CHECK (capacity > 0)
);

CREATE TABLE reservations (
    id VARCHAR(64) PRIMARY KEY,
    member_id VARCHAR(32) NOT NULL,
    class_id VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_reservations_member
        FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT fk_reservations_class
        FOREIGN KEY (class_id) REFERENCES gym_classes(id),
    CONSTRAINT uq_reservations_member_class
        UNIQUE (member_id, class_id),
    CONSTRAINT chk_reservation_status
        CHECK (status IN ('ACTIVE', 'CANCELLED'))
);

CREATE TABLE check_ins (
    id VARCHAR(64) PRIMARY KEY,
    member_id VARCHAR(32) NOT NULL,
    checked_in_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    training_session_id VARCHAR(80),
    CONSTRAINT fk_check_ins_member
        FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE INDEX idx_memberships_member_id ON memberships(member_id);
CREATE INDEX idx_gym_classes_starts_at ON gym_classes(starts_at);
CREATE INDEX idx_reservations_class_id ON reservations(class_id);
CREATE INDEX idx_check_ins_member_id ON check_ins(member_id);
