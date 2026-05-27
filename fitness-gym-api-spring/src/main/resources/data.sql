MERGE INTO members (id, name, email, qr_code) KEY(id) VALUES
('M-1001', 'Klubowicz Demo', 'demo@fitcore.local', 'QR-M-1001'),
('M-1002', 'Anna Kowalska', 'anna@example.com', 'QR-M-1002'),
('M-1003', 'Marek Nowak', 'marek@example.com', 'QR-M-1003');

MERGE INTO memberships (id, member_id, type, valid_from, valid_to) KEY(id) VALUES
('MS-M-1001-PRO', 'M-1001', 'PRO', CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE)),
('MS-M-1002-START', 'M-1002', 'START', CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE)),
('MS-M-1003-ELITE', 'M-1003', 'ELITE', CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE));

MERGE INTO trainers (id, name, specialization) KEY(id) VALUES
('T-ADAM', 'Adam Nowak', 'siła, redukcja, technika bojów'),
('T-KASIA', 'Kasia Zielińska', 'mobilność, stretching, trening funkcjonalny'),
('T-MICHAL', 'Michał Wójcik', 'trening motoryczny, przygotowanie sportowe, interwały'),
('T-OLA', 'Ola Mazur', 'trening kobiet, sylwetka, zdrowy kręgosłup');

MERGE INTO gym_classes (id, name, trainer_id, starts_at, capacity, level) KEY(id) VALUES
('C-MON-POWER-PUMP', 'Power Pump', 'T-ADAM', TIMESTAMP '2026-05-25 18:00:00', 12, 'średni'),
('C-MON-CORE', 'Core Stability', 'T-OLA', TIMESTAMP '2026-05-25 19:15:00', 14, 'lekki'),
('C-TUE-MOBILITY', 'Mobility Flow', 'T-KASIA', TIMESTAMP '2026-05-26 09:30:00', 16, 'lekki'),
('C-TUE-BOX', 'Box Fit', 'T-ADAM', TIMESTAMP '2026-05-26 19:00:00', 10, 'mocny'),
('C-WED-STRENGTH', 'Full Body Strength', 'T-MICHAL', TIMESTAMP '2026-05-27 17:30:00', 12, 'średni'),
('C-THU-YOGA', 'Stretch & Reset', 'T-KASIA', TIMESTAMP '2026-05-28 18:15:00', 18, 'lekki'),
('C-FRI-HIIT', 'HIIT Circuit', 'T-MICHAL', TIMESTAMP '2026-05-29 19:00:00', 12, 'mocny'),
('C-SAT-GLUTES', 'Glutes & Legs', 'T-OLA', TIMESTAMP '2026-05-30 10:00:00', 14, 'średni'),
('C-SUN-RECOVERY', 'Recovery Flow', 'T-KASIA', TIMESTAMP '2026-05-31 11:00:00', 20, 'lekki');

MERGE INTO reservations (id, member_id, class_id, status) KEY(id) VALUES
('R-M-1001-C-MON-POWER-PUMP', 'M-1001', 'C-MON-POWER-PUMP', 'ACTIVE');

MERGE INTO check_ins (id, member_id, training_session_id) KEY(id) VALUES
('CI-M-1001-DEMO', 'M-1001', 'WS-M-1001');
