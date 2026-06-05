INSERT INTO conversations (id, type, name, description, owner_id, avatar_url, background_url, created_at, updated_at, deleted_at)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'PRIVATE_CHAT', NULL, NULL, NULL, NULL, NULL, TIMESTAMP '2026-06-05 09:00:00', TIMESTAMP '2026-06-05 09:00:00', NULL),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'GROUP_CHAT', 'Equipo Orioneta', 'Grupo de prueba para desarrollo local', '11111111-1111-1111-1111-111111111111', NULL, NULL, TIMESTAMP '2026-06-05 09:05:00', TIMESTAMP '2026-06-05 09:05:00', NULL);

INSERT INTO conversation_participants (id, conversation_id, user_id, role, joined_at, muted, deleted_for_user)
VALUES
    ('10000000-0000-0000-0000-000000000001', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'MEMBER', TIMESTAMP '2026-06-05 09:00:00', FALSE, FALSE),
    ('10000000-0000-0000-0000-000000000002', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '22222222-2222-2222-2222-222222222222', 'MEMBER', TIMESTAMP '2026-06-05 09:00:00', FALSE, FALSE),
    ('10000000-0000-0000-0000-000000000003', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 'OWNER', TIMESTAMP '2026-06-05 09:05:00', FALSE, FALSE),
    ('10000000-0000-0000-0000-000000000004', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', 'MEMBER', TIMESTAMP '2026-06-05 09:06:00', FALSE, FALSE),
    ('10000000-0000-0000-0000-000000000005', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '33333333-3333-3333-3333-333333333333', 'MEMBER', TIMESTAMP '2026-06-05 09:07:00', FALSE, FALSE);
