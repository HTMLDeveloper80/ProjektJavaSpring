INSERT INTO members (id, name, email, qr_code) VALUES
('M-1001', 'Klubowicz Demo', 'demo@fitcore.local', 'QR-M-1001'),
('M-1002', 'Anna Kowalska', 'anna@example.com', 'QR-M-1002'),
('M-1003', 'Marek Nowak', 'marek@example.com', 'QR-M-1003');

INSERT INTO memberships (id, member_id, type, valid_from, valid_to) VALUES
('MS-M-1001-PRO', 'M-1001', 'PRO', CURRENT_DATE, CURRENT_DATE + 30),
('MS-M-1002-START', 'M-1002', 'START', CURRENT_DATE, CURRENT_DATE + 30),
('MS-M-1003-ELITE', 'M-1003', 'ELITE', CURRENT_DATE, CURRENT_DATE + 30);

INSERT INTO trainers (id, name, specialization) VALUES
('T-ADAM', 'Adam Nowak', 'siła, redukcja, technika bojów'),
('T-KASIA', 'Kasia Zielińska', 'mobilność, stretching, trening funkcjonalny'),
('T-MICHAL', 'Michał Wójcik', 'trening motoryczny, przygotowanie sportowe, interwały'),
('T-OLA', 'Ola Mazur', 'trening kobiet, sylwetka, zdrowy kręgosłup');

INSERT INTO gym_classes (id, name, trainer_id, starts_at, capacity, level) VALUES
('C-MON-POWER-PUMP', 'Power Pump', 'T-ADAM', '2026-05-25 18:00:00', 12, 'średni'),
('C-MON-CORE', 'Core Stability', 'T-OLA', '2026-05-25 19:15:00', 14, 'lekki'),
('C-TUE-MOBILITY', 'Mobility Flow', 'T-KASIA', '2026-05-26 09:30:00', 16, 'lekki'),
('C-TUE-BOX', 'Box Fit', 'T-ADAM', '2026-05-26 19:00:00', 10, 'mocny'),
('C-WED-STRENGTH', 'Full Body Strength', 'T-MICHAL', '2026-05-27 17:30:00', 12, 'średni'),
('C-THU-YOGA', 'Stretch & Reset', 'T-KASIA', '2026-05-28 18:15:00', 18, 'lekki'),
('C-FRI-HIIT', 'HIIT Circuit', 'T-MICHAL', '2026-05-29 19:00:00', 12, 'mocny'),
('C-SAT-GLUTES', 'Glutes & Legs', 'T-OLA', '2026-05-30 10:00:00', 14, 'średni'),
('C-SUN-RECOVERY', 'Recovery Flow', 'T-KASIA', '2026-05-31 11:00:00', 20, 'lekki');

INSERT INTO reservations (id, member_id, class_id, status) VALUES
('R-M-1001-C-MON-POWER-PUMP', 'M-1001', 'C-MON-POWER-PUMP', 'ACTIVE');

INSERT INTO check_ins (id, member_id, training_session_id) VALUES
('CI-M-1001-DEMO', 'M-1001', 'WS-M-1001');
