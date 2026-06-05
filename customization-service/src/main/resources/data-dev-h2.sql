INSERT INTO user_customizations (id, user_id, active_global_theme_id, active_font_id, animation_level, compact_mode, created_at, updated_at)
VALUES
    ('40000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'default', 'system', 3, FALSE, TIMESTAMP '2026-06-05 09:00:00', TIMESTAMP '2026-06-05 09:00:00'),
    ('40000000-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', 'neta-night', 'system', 4, TRUE, TIMESTAMP '2026-06-05 09:00:00', TIMESTAMP '2026-06-05 09:00:00');

INSERT INTO conversation_customizations (id, conversation_id, user_id, active_chat_theme_id, active_background_id, bubble_style, font_size, created_at, updated_at)
VALUES
    ('41000000-0000-0000-0000-000000000001', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'default-chat', 'clean-bg', 'DEFAULT', 16, TIMESTAMP '2026-06-05 09:10:00', TIMESTAMP '2026-06-05 09:10:00'),
    ('41000000-0000-0000-0000-000000000002', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', 'neta-group', 'aurora-bg', 'ROUNDED', 17, TIMESTAMP '2026-06-05 09:10:00', TIMESTAMP '2026-06-05 09:10:00');
