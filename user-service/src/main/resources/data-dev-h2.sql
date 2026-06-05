INSERT INTO users (user_id, user_name, display_name, bio, email, friend_code, status, visibility, profile_photo, created_at, updated_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'orion', 'Orion', 'Creador de Orioneta en entorno dev-h2.', 'orion@orioneta.dev', 'A91F23C70001', 'ONLINE', 'PUBLIC', 'https://local.orioneta.dev/media/avatar-orion.png', TIMESTAMP '2026-06-05 09:00:00', TIMESTAMP '2026-06-05 09:00:00'),
    ('22222222-2222-2222-2222-222222222222', 'neta', 'Neta Tester', 'Usuario de prueba para amistad y chats.', 'neta@orioneta.dev', '7B3DFF200002', 'AWAY', 'PUBLIC', NULL, TIMESTAMP '2026-06-05 09:01:00', TIMESTAMP '2026-06-05 09:01:00'),
    ('33333333-3333-3333-3333-333333333333', 'moderador', 'Moderador Demo', 'Cuenta local para revisar Neta Market.', 'moderador@orioneta.dev', '0F9A2C110003', 'OFFLINE', 'PRIVATE', NULL, TIMESTAMP '2026-06-05 09:02:00', TIMESTAMP '2026-06-05 09:02:00');
