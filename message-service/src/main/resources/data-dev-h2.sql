INSERT INTO messages (id, conversation_id, sender_id, content, type, status, created_at, updated_at, deleted_at)
VALUES
    ('20000000-0000-0000-0000-000000000001', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'Hola, este mensaje viene desde H2 dev.', 'TEXT', 'SENT', TIMESTAMP '2026-06-05 09:10:00', TIMESTAMP '2026-06-05 09:10:00', NULL),
    ('20000000-0000-0000-0000-000000000002', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '22222222-2222-2222-2222-222222222222', 'Perfecto, Swagger ya tiene datos para probar.', 'TEXT', 'READ', TIMESTAMP '2026-06-05 09:11:00', TIMESTAMP '2026-06-05 09:12:00', NULL),
    ('20000000-0000-0000-0000-000000000003', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '33333333-3333-3333-3333-333333333333', 'Primer mensaje del grupo Equipo Orioneta.', 'TEXT', 'DELIVERED', TIMESTAMP '2026-06-05 09:15:00', TIMESTAMP '2026-06-05 09:15:00', NULL);
