INSERT INTO auth_users (id, email, password_hash, display_name, avatar_url, provider, provider_user_id, role, enabled, created_at, updated_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'orion@orioneta.dev', '$2a$10$FcycEzDZnENWdIYXCtDr3.ybJysC3jD2F/Am2LAEznb.zS0hfMRG6', 'Orion', 'https://local.orioneta.dev/media/avatar-orion.png', 'EMAIL', NULL, 'USER', TRUE, TIMESTAMP '2026-06-05 09:00:00', TIMESTAMP '2026-06-05 09:00:00'),
    ('22222222-2222-2222-2222-222222222222', 'neta@orioneta.dev', NULL, 'Neta Tester', NULL, 'GOOGLE', 'google-dev-222', 'USER', TRUE, TIMESTAMP '2026-06-05 09:01:00', TIMESTAMP '2026-06-05 09:01:00'),
    ('33333333-3333-3333-3333-333333333333', 'moderador@orioneta.dev', NULL, 'Moderador Demo', NULL, 'GITHUB', 'github-dev-333', 'ADMIN', TRUE, TIMESTAMP '2026-06-05 09:02:00', TIMESTAMP '2026-06-05 09:02:00');

INSERT INTO refresh_tokens (id, auth_user_id, token_hash, created_at, expires_at, revoked_at)
VALUES
    ('90000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'dev-refresh-token-hash-orion-000000000000000000000000000000000000000000000000000000000000', TIMESTAMP '2026-06-05 09:05:00', TIMESTAMP '2026-07-05 09:05:00', NULL);
